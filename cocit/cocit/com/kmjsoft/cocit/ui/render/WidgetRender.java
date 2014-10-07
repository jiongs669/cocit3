package com.kmjsoft.cocit.ui.render;

import java.io.IOException;
import java.io.Writer;

import com.kmjsoft.cocit.ui.UIModel;
import com.kmjsoft.cocit.ui.UIRender;

public abstract class WidgetRender<T extends UIModel> implements UIRender<T> {

	protected void print(Writer out, String format, Object... args) throws IOException {
		out.write("\n");
		if (args.length == 0)
			out.write(format);
		else
			out.write(String.format(format, args));
	}

	protected void print(StringBuffer sb, String format, Object... args) {
		sb.append('\n');
		if (args.length == 0)
			sb.append(format);
		else
			sb.append(String.format(format, args));
	}

}
