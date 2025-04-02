#!/usr/bin/env groovy
/*
 * tv_remove_episodes.groovy - Filebot format expression for organizing TV shows
 *
 * This script is a variation of the tv_preset that includes individual episode
 * naming rather than season packs. Used for organizing single episodes.
 */

"/"
{'Rename me'}
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
{'/'}{n}{' - '}{t}
{
    if (f.subtitle) {subt}
}