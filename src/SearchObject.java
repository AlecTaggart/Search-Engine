
public class SearchObject implements Comparable<SearchObject> {

	private final String	location;
	private int				frequency;
	private int				position;

	public SearchObject(String p, int f, int l) {
		this.location = p;
		this.frequency = f;
		this.position = l;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int x) {
		this.frequency = x;
	}

	public void addFrequency(int x) {
		this.frequency += x;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		if (this.position > position) {
			this.position = position;
		}
	}

	public String getPath() {
		return location;
	}

	/**
	 * returns compare of two search objects
	 */
	@Override
	public int compareTo(SearchObject o) {
		if (this.frequency != o.frequency) {
			return Integer.compare(o.frequency, this.frequency);
		} else {
			if (this.position != o.position) {
				return Integer.compare(this.position, o.position);
			} else {
				return this.location.compareTo(o.location);
			}
		}
	}
}