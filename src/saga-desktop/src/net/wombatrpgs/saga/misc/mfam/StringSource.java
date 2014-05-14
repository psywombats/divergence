package net.wombatrpgs.saga.misc.mfam;

import java.util.Random;

/**
 * Legacy GAR code.
 */
public class StringSource {
	
	Random r = new Random();
	
	private String[] vowels;
	private String[] consonants;
	private String[] enders;
	private String[] starters;
	private String[] tagVowels, tagConsonants, tagEnders;

	public StringSource() {
		vowels = new String[] {"a","ai","au","e","ea","ee","ei","eu","i","ia",
				"o","oa","oi","oo","ou","u","ua"};
		consonants = new String[] {"b","bl","br","bw","by","ch","cl","cr",
				"cw","cy","cz","d","dj","dr","dw","dy","f","ff","fl","fr","fw",
				"g","gh","gl","gr","gw","h","j","jw","k",
				"kh","kk","kl","kn","kr","kw","ky","kz","l","ll","m","mn",
				"my","n","p","ph","pl","pr","pw","py","r","rh","rr","ry","s",
				"sc","sh","sk","sl","sm","sn","sp","ss","st","sw","sy",
				"t","th","tr","tt","tw","ty","tz","v","w","wh","wr","x",
				"y","z","zy"};
		starters = new String[] {"b","bl","br","by","ch","cl","cr",
				"cw","cy","cz","d","dj","dr","dw","f","fl","fr","fw",
				"g","gl","gr","gw","h","j","k",
				"kh","kl","kn","kr","l","m","mn",
				"my","n","p","ph","pl","pr","pw","py","r","rh","ry","s",
				"sc","sh","sk","sl","sm","sn","sp","st","sv","sw","sy",
				"t","th","tr","tw","tz","v","w","wh","wr","x",
				"y","z",};
		enders = new String[] {"b","by","ch","cz","d","dr","dy","ff","fy",
				"g","gh","h","k","kh","kz","l","ll","m","my","n","p","ph","r","rr",
				"s","sc","sh","sk","sp","ss","st","t","th","tt","ty","tz","v",
				"vy","w","x","z","zy","gth","ght","mb","rst","nth","rth",
				"ck","rch","lch","lth","n","ng","nk"};
		tagVowels = new String[] {"a","a","e","e","e","i","i","o","o","u","u","y"};
		tagConsonants = new String[] {"b","c","d","f","g","h","j","k","l","m",
				"n","p","r","s","t","v","w","z"};
		tagEnders = new String[] {"b","d","g","h","k","l","m","n","p","r","t","v","w",};
	}
	
	/**
	 * @return a tag not currently in use
	 */
	public String getTag() {
		String tag = "";
		if (r.nextBoolean()) {
			tag += tagConsonants[r.nextInt(tagConsonants.length)];
			tag += tagVowels[r.nextInt(tagVowels.length)];
			tag += tagEnders[r.nextInt(tagEnders.length)];
		} else {
			tag += tagVowels[r.nextInt(tagVowels.length)];
			tag += tagConsonants[r.nextInt(tagConsonants.length)];
			tag += tagVowels[r.nextInt(tagVowels.length)];
		}
		return tag;
	}
	
	/**
	 * @return a random vaguely-pronouncable string name.
	 */
	public String randomName() {
		if (MFamConstants.FUN_MODE) {
			String name = vowels[r.nextInt(vowels.length)];
			if (r.nextBoolean()) {
				name += consonants[r.nextInt(consonants.length)];
				name += vowels[r.nextInt(vowels.length)];
			}
			if (r.nextInt(100) < 40) name += enders[r.nextInt(enders.length)];
			if (r.nextInt(100) < 70 || name.length() < 5) {
				name = starters[r.nextInt(starters.length)]+name;
			}
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			if (name.length() > 8) {
				name = name.substring(0, 8);
			}
			return name;
		} else {
			return String.valueOf(r.nextInt(999999));
		}
	}

}
