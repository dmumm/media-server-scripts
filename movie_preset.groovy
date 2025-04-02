#!/usr/bin/env groovy
/*
 * movie_preset.groovy - Filebot format expression for organizing movies
 *
 * This script determines the appropriate folder structure and naming convention
 * for movie files based on their technical properties (bitrate, codec, etc.)
 * and metadata.
 */

"/"
{
    // Determine top-level folder based on video quality
    if (((bitrate > 30e6) && (hd==/UHD/)) || ((bitrate > 10e6) && (hd==/HD/))) 
        'Movies Remux' 
    else
        {vc =~ /HEVC|265/ ? 'Movies H.265' : 'Movies H.264'}
}
{'/!!! HARD-LINKED'}/{plex[1]}{'/'}{plex[2]}{allOf{tags.join(', ')}{vs}
{
    // Determine quality label based on bitrate and resolution
    if (((bitrate > 30e6) && (hd==/UHD/)) || ((bitrate > 10e6) && (hd==/HD/))) 
        'Remux' 
    else    
        {vc.replace('AVC','x264').replace('HEVC','x265')}
}{hd}{hdr}
{
    // Audio track processing
    def preferredLang = 'Eng'
    def useChFilter = false
    def filter = { [it.codec, it.ch, it.objects, it.lang].findAll() }

    // Codec mapping for standardized naming
    def codecList =
    [
    'MPEG Audio' : 'MP2',
    'MP3' : 'MP3',
    'PCM' : 'PCM',
    'FLAC' : 'FLAC',
    'AAC LC' : 'AAC',
    'AAC LC SBR' : 'AAC',
    'AAC LC SBR PS' : 'AAC',
    'AC 3' : 'AC3',
    'AC 3 Dep' : 'EAC3',
    'E AC 3' : 'EAC3',
    'E AC 3 JOC' : 'EAC3 Atmos',
    'AC 3 Dep JOC' : 'EAC3 Atmos',
    'DTS' : 'DTS',
    'DTS 96 24' : 'DTS 96-24',
    'DTS ES' : 'DTS-ES',
    'DTS ES XXCH' : 'DTS-ES',
    'DTS XBR' : 'DTS-HD HRA',
    'DTS ES XBR' : 'DTS-HD HRA',
    'DTS ES XXCH XBR' : 'DTS-HD HRA',
    'DTS XLL' : 'DTS-HD MA',
    'DTS ES XLL' : 'DTS-HD MA',
    'DTS ES XXCH XLL' : 'DTS-HD MA',
    'DTS XLL X' : 'DTS X',
    'MLP FBA' : 'TrueHD',
    'MLP FBA 16 ch' : 'TrueHD Atmos'
    ]

    def audioStreams = []
    def audioClean = { it.replaceAll(/\p{Punct}/, ' ') }
    def channelClean = { it.replaceAll(/Debug.+|Object\sBased\s?\/?|(\d+)?\sobjects\s\/\s|0.(?=\d.\d)|20/).replaceAll(/6.0/,'5.1').replaceAll(/8.0/,'7.1') }
    def listStream = { it.sort{ a, b -> b.bitrate <=> a.bitrate }.collect{ filter(it) }.unique()*.join(' ') }
    def oneStream = { listStream(it)[0] }
    def dString = { it.toDouble().toString() }
    def toInt = { it.toInteger() }

    // Process each audio stream
    any{audio.collect{ au ->
        def codec = audioClean(any{ au['CodecID/Hint'] }{ au['Format'] })
        def format_profile = any{ audioClean(au['Format_AdditionalFeatures'])}{}
        def String ch = any{ channelClean(au.ChannelPositionsString2).tokenize('\\/')*.toDouble().sum() }
            { channelClean(au.ChannelLayout_Original).split().collect{ it == 'LFE' ? 0.1 : 1 }.sum() }
            { channelClean(dString(au.ChannelsOriginal)) } { channelClean(dString(au.Channels)) }

        def chFilter = ( ( ( (ac == 'AAC'||ac == 'MP3') && ch != '2.0') || ( (ac == 'AC3'||ac == 'EAC3'||ac == 'DTS'||ac == 'TrueHD'||ac == 'MLPFBA') && ch != '5.1' ) ) ? ch : null )

        def combined = allOf{codec}{format_profile}.join(' ')

        // Create stream info object
        audioStreams << ['index' : codecList.findIndexOf { it.key == combined }, 'default' : any {au['default'][0].toBoolean() }{ audio.size == 1 ? true : '' },
        'codec' : codecList.get(combined, 'Add to "' + combined + '" codecList'), 'combined' : combined, 'ch' : useChFilter ? chFilter : ch,
        'bitrate' : any{ toInt(au.BitRate) }{ toInt(au.BitRate_Maximum) }{ au.FrameRate.toDouble() }{null},
        'objects' : any{def objects = au['NumberOfDynamicObjects']; objects ? "[$objects Objs]" : ''}{null}, 'lang' : any{ au.'LanguageString3'.upperInitial() }{null} ]
        return audioStreams
    }

    // Find best audio stream
    def addToList = audioStreams.codec.findAll{ it.contains('Add to') }.unique().sort()
    def allStreams = listStream(audioStreams)
    def preferredStream = oneStream(audioStreams.findAll{ it.index == audioStreams.index.max() })
    def bestBitRate = oneStream(audioStreams.findAll{ it.bitrate == audioStreams.bitrate.max() })
    def defaultStream = any{ oneStream(audioStreams.findAll{ it.default == true }) }{ oneStream(audioStreams.findAll{ !it.default }) }
    def bestPreferredLang = any{ oneStream(audioStreams.findAll{ it.lang == preferredLang }) }{}

    // Return the best audio stream information
    any{addToList}{bestPreferredLang}{defaultStream}{bestBitRate}{preferredStream}
    }{'NO_AUDIO'}
}.joining('] [', ' [', ']')} [{ext}] {if (dc > 1) " ($di)"}