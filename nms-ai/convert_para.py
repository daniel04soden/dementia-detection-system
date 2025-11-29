# Python program created as a means of taking user speech and formatting it correctly for analysis

# Example Paragraph:

from string import punctuation

# cc - non dementia
"""
*PAR:	all of the action you see going on . [+ exc] 
*PAR:	okay . [+ exc] 
*PAR:	this is in the kitchen . 
*PAR:	and the little boy is climbing up to the cookie jar and he's [//]
*PAR:	the mother has her back turned . 
*PAR:	she's standing at the sink washing dishes . 
*PAR:	she's probably looking out the window (be)cause the sink is running
*PAR:	the stool is tipping for the little boy and he's going to fall off
*PAR:	and the window's open xxx .
*PAR:	the water is running in the sink is why it's xxx out . 
*PAR:	now I see . [+ exc] 
*PAR:	the mother's drying a dish . 
*PAR:	+< mhm . [+ exc] 
"""

# cd - has dementia
"""
*PAR:	oh yes . [+ exc] 
*PAR:	a little girl &a and the little boy is getting cookies out_of the
*PAR:	oh and the stool is upsetting &=laughs . 
*PAR:	and the [//] I guess it's the mother is drying dishes . 
*PAR:	water's spilling down all over the sink . 
*PAR:	water on the floor . [+ gram] 
*PAR:	little boy's gonna fall and that [/] &steps &uh that there stool .
*PAR:	&uh I think the cookie jar is gonna fall &=laughs . 
*PAR:	and the [//] guess dryin(g) dishes and water [//] the faucet's on
*PAR:	(..) &wh <what else am I [/] &sup I_mean what> [//] what +..?
*PAR:	yeah . [+ exc] 
*PAR:	the cookie jar . [+ gram] 
*PAR:	the stool . [+ gram] 
*PAR:	her doin(g) the dishes . [+ gram] 
*PAR:	the water spilling . 
*PAR:	<far as> [//] yeah that's as far as I could see . [+ exc] 
"""


def detect_punc_para(user_transcript: str):
    """
    This funtion reads a paragraph and identifies all punctual symbols
    that may be used to denote different speech patterns as a means of finding out what each
    punctual symbol may mean - made for personal use
    """
    res_symbols = set()
    addit_symbols = set(punctuation)
    addit_symbols.update(["&", "<", ">"])  # Add key annotation markers
    print(addit_symbols)
    s = user_transcript.split()
    for words in s:
        words = words.strip()
        if "*PAR:" in words or "'s" in words:
            continue
        elif any(char in addit_symbols for char in words):
            res_symbols.update([words])
            print(words)
    print(res_symbols)


def main():
    para = """
    *PAR:	oh yes . [+ exc] 
    *PAR:	a little girl &a and the little boy is getting cookies out_of the
    *PAR:	oh and the stool is upsetting &=laughs . 
    *PAR:	and the [//] I guess it's the mother is drying dishes . 
    *PAR:	water's spilling down all over the sink . 
    *PAR:	water on the floor . [+ gram] 
    *PAR:	little boy's gonna fall and that [/] &steps &uh that there stool .
    *PAR:	&uh I think the cookie jar is gonna fall &=laughs . 
    *PAR:	and the [//] guess dryin(g) dishes and water [//] the faucet's on
    *PAR:	(..) &wh <what else am I [/] &sup I_mean what> [//] what +..?
    *PAR:	yeah . [+ exc] 
    *PAR:	the cookie jar . [+ gram] 
    *PAR:	the stool . [+ gram] 
    *PAR:	her doin(g) the dishes . [+ gram] 
    *PAR:	the water spilling . 
    *PAR:	<far as> [//] yeah that's as far as I could see . [+ exc] 
    """
    detect_punc_para(para)


if __name__ == "__main__":
    main()
