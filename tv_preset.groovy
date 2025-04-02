#!/usr/bin/env groovy
/*
 * tv_preset.groovy - Filebot format expression for organizing TV shows
 *
 * This script determines the appropriate folder structure and naming convention
 * for TV show files based on their technical properties (bitrate, codec, etc.)
 * and metadata.
 */

"/"
{
    // Determine top-level folder based on video quality
    if (((any{vbr}{bitrate} > 30e6) && (hd==/UHD/)) || ((any{vbr}{bitrate} > 10e6) && (hd==/HD/))) 
        'TV Remux' 
    else
        {vc =~ /HEVC|265/ ? 'TV H.265' : 'TV H.264'}
}
{'/'}{plex[1]}
{'/'}{plex[2]}
{allOf
    // Add tags and special edition indicators
    {allOf
        {tags.join(', ')}
        {fn =~ /(?i)WEBRip/ ? 'WEBRip' : vs.replace('BluRay', 'BR')}
        {f.match(/(?i)matte/) ? 'Open Matte' : null}
        {f.match(/(?i).DC.| DC |\[DC\]/) ? 'Director\'s Cut' : null}
        {f.match(/(?i) DC /) ? 'Director\'s Cut' : null}
        {f.match(/(?i)regrade/) ? 'Regrade' : null}
        {f.match(/(?i)fangrade/) ? 'Fangrade' : null}
        {f.match(/(?i)upscale/) ? 'Upscale' : null}
        {f.match(/(?i)legendary/) ? 'Legendary' : null}
    .joining(', ', '', '')}
.joining('] [', ' [', ']')}
{'/'}{plex[3]}
{allOf
    // Technical details section
    {allOf
        {fn =~ /(?i)WEBRip/ ? '[WEBRip' : vs.replace('BluRay', 'BR')}
        {vf}
        {hdr.replace('Dolby Vision', 'DV')}
        {vc.replace('AVC','x264').replace('HEVC','x265').replace('Microsoft', 'VC-1')}
        {any{vbr}{'<'+bitrate}.replace(' Mbps', 'mbps')}
        .joining(' ','','')}
    {group.replace('DC','').replace('ARROW','')}
    {audio.title =~ /(?i)Commentary/ ? 'Commentary' : null}
.joining('] [', ' [', ']')}
{
    if (f.subtitle) {subt}
}