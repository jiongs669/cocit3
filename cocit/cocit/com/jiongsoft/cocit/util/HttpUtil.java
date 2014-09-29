package com.jiongsoft.cocit.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class HttpUtil {

	public static void writeJson(HttpServletResponse response, String json) throws IOException {
		write(response, json, "text/json; charset=UTF-8");
	}

	public static void writeHtml(HttpServletResponse response, String html) throws IOException {
		write(response, html, "text/html; charset=UTF-8");
	}

	public static void writeXml(HttpServletResponse response, String xml) throws IOException {
		write(response, xml, "text/xml; charset=UTF-8");
	}

	public static void write(HttpServletResponse response, String content, String contentType) throws IOException {
		PrintWriter writer = null;
		try {
			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Throwable ex) {
					Log.error("HttpUtil.write: error!", ex);
				}
			}
		}
	}

	public static void write(HttpServletResponse response, File file) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			String extName = file.getName();
			extName = extName.substring(extName.lastIndexOf(".") + 1);
			String attName = file.getName();
			response.setHeader("Content-Disposition", "attachement; filename=" + attName);
			response.setContentType("application/octet-stream");
			inStream = new FileInputStream(file);
			outStream = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Throwable ex) {
					Log.error("HttpUtil.write: error!", ex);
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
					Log.error("HttpUtil.write: error!", ex);
				}
			}
		}
	}

	/**
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 生成随机验证码，验证码被保存在session(key=cocit_verify_code)中，可以通过{@link #checkVerificationCode(String)进行验证 。
	 */
	public static String makeSmsVerifyCode(HttpServletRequest request, String mobile) {

		if (!StringUtil.isMobile(mobile)) {
			throw new CocException("非法手机号码！");
		}

		StringBuffer sb = new StringBuffer();

		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		Random r = new Random();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			sb.append(ch[index]);
		}

		HttpSession session = request.getSession(true);

		session.setAttribute("cocit_verify_mobile", mobile);
		session.setAttribute("cocit_verify_code", sb.toString());

		return sb.toString();
	}

	/**
	 * 检查客户端提交的验证码是否与session(key=cocit_verify_code)中保存的验证码一致？如果不一致，将抛出异常。
	 * <p>
	 * 检查时将忽略大小写。
	 * 
	 * @param code
	 * @param exceptionMessage
	 */
	public static void checkSmsVerifyCode(HttpServletRequest request, String mobile, String code, String exceptionMessage) {

		if (StringUtil.isNil(exceptionMessage))
			exceptionMessage = "验证码或手机号不正确！";

		HttpSession session = request.getSession(true);

		String validMobile = (String) session.getAttribute("cocit_verify_mobile");
		String validCode = (String) session.getAttribute("cocit_verify_code");

		if (StringUtil.isNil(code) || StringUtil.isNil(validCode) || StringUtil.isNil(mobile) || StringUtil.isNil(validMobile))
			throw new CocException(exceptionMessage);

		if (!code.toLowerCase().equals(validCode.toLowerCase()) || !mobile.toLowerCase().equals(validMobile.toLowerCase()))
			throw new CocException(exceptionMessage);

		session.removeAttribute("cocit_verify_mobile");
		session.removeAttribute("cocit_verify_code");
	}

	/**
	 * 生成随机图片验证码，并将图片对象输出到response中
	 * <p>
	 * 验证码文本被保存在session(key=cocit_verify_code)中，可以通过{@link #checkVerificationCode(String)}进行验证 。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String makeImgVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		// 生成彩色背景
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		g.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD | Font.PLAIN, 18));

		StringBuffer sb = new StringBuffer();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(random.nextInt(88), random.nextInt(188), random.nextInt(255)));

			// 写什么数字，在图片 的什么位置画
			index = random.nextInt(len);
			g.drawString("" + ch[index], (i * 14) + 5, 16);

			sb.append(ch[index]);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("cocit_verify_code", sb.toString());

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		OutputStream out = null;
		try {
			out = response.getOutputStream();

			ImageIO.write(image, "JPEG", out);
		} catch (IOException ex) {
			Log.error("HttpUtil.makeImgVerifyCode: error! ", ex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					Log.error("HttpUtil.makeImgVerifyCode: error! ", ex);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 检查客户端提交的验证码是否与session(key=cocit_verify_code)中保存的验证码一致？如果不一致，将抛出异常。
	 * <p>
	 * 检查时将忽略大小写。
	 * 
	 * @param code
	 * @param exceptionMessage
	 */
	public static void checkImgVerifyCode(HttpServletRequest request, String code, String exceptionMessage) {

		if (StringUtil.isNil(exceptionMessage))
			exceptionMessage = "验证码不正确！";

		HttpSession session = request.getSession(true);

		String validCode = (String) session.getAttribute("cocit_verify_code");

		if (StringUtil.isNil(code) || StringUtil.isNil(validCode))
			throw new CocException(exceptionMessage);

		if (!code.toLowerCase().equals(validCode.toLowerCase()))
			throw new CocException(exceptionMessage);

		session.removeAttribute("cocit_verify_code");
	}
}
