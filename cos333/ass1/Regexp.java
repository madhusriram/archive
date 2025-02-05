public class Regexp {
	/**
	 * @param args
	 */
	private String regexp;
	private int relen;
	private int repos;
	private char char1;
	private char char2;
	private int start;
	private int end;
	private int stringpos;
	private boolean fixedend;

	public Regexp(String re) {

		this.regexp = re;
		this.relen = regexp.length();
		this.repos = 0;
		this.stringpos = 0;
		if (this.relen >= 1) {
			this.char1 = this.regexp.charAt(0);
		}
		if (this.relen >= 2) {
			this.char2 = this.regexp.charAt(1);
		}
		this.start = -1;
		this.end = -1;
		this.fixedend = false;
	}

	public boolean match(String s) // returns true if the regular expression
									// matches s, and false if not.
	{
		boolean ret;
		int index = 0;
		if (this.char1 == '^') {
			this.increment(1);
			ret = matchhere(s);
			if (ret == true) {
				this.start = 0;
			} else {
				this.start = -1;
				this.end = -1;
			}
			return ret;
		}

		do {
			ret = matchhere(s);
			if (ret == true) {
				this.start = index;
				this.end = this.stringpos;
				return ret;
			} else {
				index++;
				if (s.length() == 0) {
					return false;
				}
				s = s.substring(1);
				this.stringpos++;
			}
		} while (s.length() > 0);

		this.start = -1;
		this.end = -1;
		return false;

	}

	private boolean decrement(int n) {
		assert (n <= 2);

		if (n == 2) {
			if (this.repos - 2 >= 0) {
				this.char1 = this.regexp.charAt(this.repos - 2);
				this.char2 = this.regexp.charAt(this.repos - 1);
				this.repos -= 2;
			} else {
				this.char1 = this.regexp.charAt(0);
				if (this.regexp.length() > 0) {
					this.char2 = this.regexp.charAt(1);
				} else {
					this.char2 = '\0';
				}
				this.repos--;
			}
		}
		if (n == 1) {
			if (this.regexp.length() > 1) {
				this.char2 = this.char1;
			} else {
				this.char2 = '\0';
			}
			this.char1 = regexp.charAt(this.repos - 1);
			this.repos--;
		}
		return false;
	}

	private boolean increment(int n) {
		assert (n <= 2);
		/*
		 * while ((this.repos + n + 2) > this.relen) { n--; }
		 */
		if (n == 2) {
			if (this.repos + 3 < this.relen) {
				this.char1 = this.regexp.charAt(this.repos + 2);
				this.char2 = this.regexp.charAt(this.repos + 3);
				this.repos += 2;
			} else if (this.repos + 2 < this.relen) {
				this.char1 = this.regexp.charAt(this.repos + 2);
				this.char2 = '\0';
				this.repos++;
			} else {
				this.char1 = '\0';
				this.char2 = '\0';
			}
		}
		if (n == 1) {

			if (this.repos + 2 < this.relen) {
				this.char1 = this.char2;
				this.char2 = this.regexp.charAt(this.repos + 2);
				this.repos++;
			} else if (this.char1 == '\0') {
				this.char1 = '\0';
				this.char2 = '\0';
			} else {
				this.char1 = this.char2;
				this.char2 = '\0';
			}
		}
		return false;
	}

	private boolean matchhere(String s) {
		char c;
		// to quiet warnings
		c = '\0';
		if (c == '\0')
			c = '\0';

		if (this.char1 == '\0') {
			this.end = this.stringpos;
			return true;
		}
		if (this.char2 == '*') {
			return matchstar(s);
		}
		if (this.char2 == '?') {
			return matchqmark(s);
		}
		if (this.char2 == '+') {
			return matchplus(s);
		}
		if (this.char1 == '$' && (repos + 2 >= relen)) {
			if (s.length() == 0 ) {
				end = this.stringpos;
				return true;
			} else {
				return false;
			}
		}

		if (this.char2 == '$' && this.repos + 2 >= this.relen) {
			if (s.length() == 1) {
				if (s.charAt(0) == this.char1 || this.char1 == '.') {
					this.stringpos++;
					end = this.stringpos;
					return true;
				}
			}
			return false;
		}

		if (s.length() > 1) {
			c = s.charAt(1);
		} else {
			c = '\0';
		}

		if (s.length() != 0 && (this.char1 == '.' || s.charAt(0) == this.char1)
				&& (this.char2 == '.' || this.char2 == '\0' || c == this.char2)) {

			this.stringpos++;
			this.increment(1);

			if (s.length() >= 1) {
				return matchhere(s.substring(1));
			}

			/*
			 * if (s.length() != 0 && (this.char1 == '.' || s.charAt(0) ==
			 * this.char1) && (this.char2 == '.' || this.char2 == '\0' || c ==
			 * this.char2)) { if (this.char2 == '\0') { this.stringpos++; } else
			 * { this.stringpos += 2; } this.increment(2); if (s.length() >= 2)
			 * { return matchhere(s.substring(2)); } else if (s.length() >= 1) {
			 * return matchhere(s.substring(1)); } }
			 */

		}
		return false;
	}

	private boolean matchstar(String s) {

		assert (this.char2 == '*');

		boolean ret;
		char c;

		if (s.length() > 0) {
			c = s.charAt(0);
		} else {
			end = this.stringpos;
			return true;
		}
		int i;

		// should c=s.charAt(i) be increment condition?
		for (i = 0; i < s.length() && (c == this.char1 || this.char1 == '.');) {
			c = s.charAt(i);
			if(c == this.char1 || this.char1 == '.'){
				i++;
			}
		}
		this.increment(2);
		do {
			// increment before matchhere?
			ret = matchhere(s.substring(i));
			if (ret == true) {
				this.stringpos += i;
				if (this.char1 == '\0' && this.char2 == '\0') {
					this.end = this.stringpos;
				}
				return ret;
			}
		} while (i-- > 0);
		this.decrement(2);
		return false;
	}

	private boolean matchplus(String s) {

		assert (this.char2 == '+');

		int i;
		char c;
		String copy = s;
		boolean ret;

		if (s.length() == 0) {
			return false;
		}

		if (s.charAt(0) != this.char1 && this.char1 != '.') {
			return false;
		}

		this.stringpos++;
		copy = s.substring(1);

		if (copy.length() > 0) {
			c = copy.charAt(0);
		} else {
			c = '\0';
		}

		i = 0;
		while (i < copy.length() && (c == this.char1 || this.char1 == '.')) {
			c = copy.charAt(i);
			if (c == this.char1 || this.char1 == '.') {
				i++;
			}
		}/*
		 * for (i = 0; i < copy.length() && (c == this.char1 || this.char1 ==
		 * '.'); i++) { c = s.charAt(i); }
		 */
		this.increment(2);
		do {
			// increment before matchhere?
			ret = matchhere(copy.substring(i));
			if (ret == true) {
				this.stringpos += i;
				if (this.char1 == '\0' && this.char2 == '\0') {
					this.end = this.stringpos;
				}
				return ret;
			}
		} while (i-- > 0);

		this.decrement(2);
		return false;

	}

	private boolean matchqmark(String s) {
		assert (this.char2 == '?');

		boolean ret;
		int index = 0;
		char charone = this.char1;

		this.increment(2);

		if (s.length() == 0) {
			return matchhere(s);
		}
		// TODO: does order matter?
		// increment before matchhere?
		// matches 0
		if (s.charAt(0) == charone || charone == '.') {
			index++;
			ret = matchhere(s.substring(1));
			if (ret == true) {
				this.stringpos += index;

				this.end = this.stringpos;

				return ret;
			}
		}

		ret = matchhere(s);
		if (ret == false) {
			this.decrement(2);
		}
		return ret;

	}

	public int start() // returns the index of the first character of the
						// matched substring, or -1 if there was no match.
	{
		return this.start;

	}

	public int end() // returns the index of one beyond the last character of
						// the matched substring, or -1 if there was no match.
	{
		if (this.fixedend == true) {
			this.end--;
		}
		return this.end;
	}

}
