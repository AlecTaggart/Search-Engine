import java.nio.file.Path;
import java.util.ArrayList;

public class ThreadSafeInvertedIndex extends InvertedIndex {

	private ReadWriteLock lock;

	public ThreadSafeInvertedIndex() {
		super();
		this.lock = new ReadWriteLock();
	}

	@Override
	public void add(String word, String path, int position) {
		lock.lockReadWrite();
		try {
			super.add(word, path, position);

		} finally {
			lock.unlockReadWrite();
		}
	}

	@Override
	public void addAll(InvertedIndex index) {
		lock.lockReadWrite();
		try {
			super.addAll(index);

		} finally {
			lock.unlockReadWrite();
		}
	}

	@Override
	public boolean containsWord(String word) {
		lock.lockReadOnly();
		try {
			return super.containsWord(word);

		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public int size() {
		lock.lockReadOnly();
		try {
			return super.size();

		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public boolean containsFile(String word, String file) {
		lock.lockReadOnly();
		try {
			return super.containsFile(word, file);

		} finally {
			lock.unlockReadOnly();
		}

	}

	@Override
	public boolean writeWords(Path path) {
		lock.lockReadOnly();
		try {
			return super.writeWords(path);

		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public String toString() {
		lock.lockReadOnly();
		try {
			return super.toString();

		} finally {
			lock.unlockReadOnly();
		}

	}

	@Override
	public ArrayList<SearchObject> partialSearch(String[] queryWords) {
		lock.lockReadOnly();
		try {
			return super.partialSearch(queryWords);
		} finally {
			lock.unlockReadOnly();
		}
	}

	@Override
	public ArrayList<SearchObject> exactSearch(String[] queryWords) {
		lock.lockReadOnly();
		try {
			return super.exactSearch(queryWords);
		} finally {
			lock.unlockReadOnly();
		}
	}
}
