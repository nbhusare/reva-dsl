package reva.ide.contentassist.providers;

public class Snippet {
	private String insertText;
	private String label;
	private int priority;

	public String getInsertText() {
		return insertText;
	}

	public void setInsertText(String insertText) {
		this.insertText = insertText;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
