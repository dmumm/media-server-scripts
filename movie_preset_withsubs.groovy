"/"
{
    if (((any{vbr}{bitrate} > 30e6) && (hd==/UHD/)) || ((any{vbr}{bitrate} > 10e6) && (hd==/HD/))) 
        'Movies Remux' 
    else
        {vc =~ /HEVC|265/ ? 'Movies H.265' : 'Movies H.264'}
}
{'/!!! HARD-LINKED'}{'/'}{plex[1]}{'/'}
{ny}
{allOf
	{allOf
		{tags.join(', ')}
		{f.match(/(?i)matte/) ? 'Open Matte' : null}
		{f.match(/(?i).DC.| DC |\[DC\]/) ? 'Director\'s Cut' : null}
		{f.match(/(?i) DC /) ? 'Director\'s Cut' : null}
		{f.match(/(?i)regrade/) ? 'Regrade' : null}
		{f.match(/(?i)fangrade/) ? 'Fangrade' : null}
		{f.match(/(?i)upscale/) ? 'Upscale' : null}
		{f.match(/(?i)legendary/) ? 'Legendary' : null}
	.joining(', ', '', '')}
	{ 
	if (f =~ /(?i)criterion/)
		return "Criterion"
	else if (f =~ /(?i)arrow/)
		return "Arrow"
	else if (f =~ /(?i)shout|(?i)scream/ && f =~ /(?i)factory/)
		return "Shout!"
	else if (f =~ /(?i)studio/ && f =~ /(?i)canal/)
		return "StudioCanal"
	else if (f =~ /(?i)HKR/ || f =~ /(?i)hongkongrescue/ || f =~ /(?i)hong kong rescue/ || f =~ /(?i)hong.kong.rescue/)
		return "HKR"
	else if (f =~ /(?i)kino|(?i)lorber/)
		return "Kino Lorber"
	else if (f =~ /(?i)Eureka/)
		return "Eureka"
	else if (f =~ /(?i)gbr/)
		return "GBR"
	else if (f =~ /(?i)hybrid/)
		return "Hybrid"
	}
	{allOf
		{fn =~ /(?i)WEBRip/ ? 'WEBRip' : vs.replace('BluRay', 'BR')}
		{vf}
		{hdr.replace('Dolby Vision', 'DV')}
		{vc.replace('Microsoft', 'VC-1')}
		{any{vbr}{'<'+bitrate}.replace(' Mbps', 'mbps')}
		.joining(' ', '', '')}
	{audio.title =~ /(?i)Commentary/ ? 'Commentary' : null}
	{group.replace('DC','').replace('ARROW','')}
.joining('] [', ' [', ']')}
{if (f.subtitle) {subt}}