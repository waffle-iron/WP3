package eu.modelwriter.writer.markers.views.internal;

import eu.modelwriter.writer.markers.actions.MarkElement;

public class MappingViewLengthColumn extends MappingViewColumn {

	@Override
	public String getText(Object element) {
		if (element instanceof MarkElement) {
			return Integer.toString(((MarkElement) element).getLength());
		}
		return "";
	}

	@Override
	public String getTitle() {
		return "Length";
	}

}
