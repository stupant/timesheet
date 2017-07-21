/*
 * {{{ header & license
 * Copyright (c) 2016 Farrukh Mirza
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * }}}
 */


/**
 * @author Farrukh Mirza
 * 24/06/2016 
 * Dublin, Ireland
 */
package com.wbs.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder.FontStyle;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder.TextDirection;

@Service
public class PdfConverter {
	private static final Logger logger = LoggerFactory.getLogger(PdfConverter.class);

	public void convertHtmlToPdf(String html, OutputStream out) {
		convertHtmlToPdf(html, null, out);
	}

//	public void convertHtmlToPdf(String html, String css, OutputStream out) {
//		try {
//			html = correctHtml(html);
//			html = getFormedHTMLWithCSS(html, css);
//
//			ITextRenderer r = new ITextRenderer();
//			r.getFontResolver().addFontDirectory(getClass().getClassLoader().getResource("fonts").getPath(), false);
//			r.setDocumentFromString(html);
//			r.layout();
//			r.createPDF(out);
//			r.finishPDF();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//		}
//	}
	
	public void convertHtmlToPdf(String html, String css, OutputStream out) {
		PdfRendererBuilder builder = new PdfRendererBuilder();
		String fontPath = getClass().getClassLoader().getResource("fonts").getPath();
//		css += "@font-face { font-family: 'Roboto'; src: url('file://" + fontPath + "/Roboto-Regular.ttf');  }";
//		css += "@font-face { font-family: 'Roboto'; src: url('file://" + fontPath + "/Roboto-Bold.ttf'); font-weight: bold; }";
//		css += "@font-face { font-family: 'Roboto'; src: url('file://" + fontPath + "/Roboto-Italic.ttf'); font-style: italic; }";
//		css += "@font-face { font-family: 'Roboto'; src: url('file://" + fontPath + "/Roboto-BoldItalic.ttf'); font-weight: bold; font-style: italic; }";
//		css += "body, body * { font-family: 'Roboto'; }";
		
		css += "@font-face { font-family: 'NotoSans'; src: url('file://" + fontPath + "/NotoSans-Regular.ttf');  }";
		css += "@font-face { font-family: 'NotoSans'; src: url('file://" + fontPath + "/NotoSans-Bold.ttf'); font-weight: bold; }";
		css += "@font-face { font-family: 'NotoSans'; src: url('file://" + fontPath + "/NotoSans-Italic.ttf'); font-style: italic; }";
		css += "@font-face { font-family: 'NotoSans'; src: url('file://" + fontPath + "/NotoSans-BoldItalic.ttf'); font-weight: bold; font-style: italic; }";
		css += "body, body * { font-family: 'NotoSans'; }";
//		builder.useFont(new File(fontPath), "vie");
//		builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
//	   builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
//	   builder.defaultTextDirection(TextDirection.RTL); // OR RTL
//		builder.useFont(new File(getClass().getClassLoader().getResource("fonts/Roboto-Regular.ttf").getPath()), "sans-serif", 100, FontStyle.NORMAL, false);
//		builder.useFont(new File(getClass().getClassLoader().getResource("fonts/Roboto-Bold.ttf").getPath()), "sans-serif", 400, FontStyle.NORMAL, false);
//		builder.useFont(new File(getClass().getClassLoader().getResource("fonts/Roboto-BoldItalic.ttf").getPath()), "sans-serif", 400, FontStyle.ITALIC, false);
//		builder.useFont(new File(getClass().getClassLoader().getResource("fonts/Roboto-Italic.ttf").getPath()), "sans-serif", 100, FontStyle.ITALIC, false);
//		builder.useFont(new File(getClass().getClassLoader().getResource("fonts/NotoMono-Regular.ttf").getPath()), "monospace");
//		builder.useReplacementText("^");
		builder.toStream(out);
		builder.withHtmlContent(getFormedHTMLWithCSS(correctHtml(html), css), "");
		try {
			logger.info(getFormedHTMLWithCSS(correctHtml(html), css));
			builder.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void convertHtmlToPdf(List<String> htmls, OutputStream out) {
		convertHtmlToPdf(htmls, null, out);
	}

	public void convertHtmlToPdf(List<String> htmls, String css, OutputStream out) {
		try {
			PDFMergerUtility merge = new PDFMergerUtility();

			for (String html : htmls) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				// convertHtmlToPdf() performs null check on css by default, so
				// no need to do it here.
				convertHtmlToPdf(html, css, bos);

				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				merge.addSource(bis);
			}

			merge.setDestinationStream(out);
			merge.mergeDocuments(null);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}

	private String getFormedHTMLWithCSS(String htmlBody, String css) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head><meta charset='utf-8'/>");
		if (StringUtils.isNotBlank(css)) {
			sb.append("<style type='text/css'>");
			sb.append(css);
			
			sb.append("</style>");
//			sb.append("<style type='text/css'>@font-face { font-family: 'Noto Sans'; src: url('Roboto-Regular.ttf'); body, body * { font-family: 'Noto Sans', sans-serif; }</style>");
		}
		sb.append("</head>");
		sb.append("<body>");
		sb.append(htmlBody);
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	private String correctHtml(String html) {
		html = html.replaceAll("&nbsp;", "&#160;");

		return html;
	}

}
