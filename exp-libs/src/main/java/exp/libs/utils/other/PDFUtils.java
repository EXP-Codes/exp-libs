package exp.libs.utils.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.security.SecureRandom;

/**
 * FIXME 
 * 仅用于linux环境
 * 通过 google-chrome 工具把指定页面打印成 pdf
 */
public final class PDFUtils {

	// 此处有命令注入问题，待修改
	// 最后一个 %s 可以通过 ; 执行多条命令被命令行注入
    private static final String URL2PDF_COMMAND_TEMPLATE = "mkdir -p /tmp/pdf && "
            + "google-chrome --headless --disable-gpu --print-to-pdf=/tmp/pdf/%s --hide-scrollbars '%s'";

    public static String url2pdf(String url) throws InterruptedException, IOException {
        if (null == url || url.isEmpty()) {
            throw new IllegalArgumentException("url can not be empty");
        }

        if (!url.matches("^https?:\\/\\/.+$")) {
            throw new MalformedURLException("Illegal URL");
        }

        String filename = randomId() + ".pdf";
        String path = "/tmp/pdf/" + filename;
        String command = String.format(URL2PDF_COMMAND_TEMPLATE, filename, url);
        Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
//        process.waitFor(30, TimeUnit.SECONDS);
        return path;
    }

    public static String readAll(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuffer output = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        return output.toString();
    }

    public static String randomId() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return byteArrayToHex(bytes);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

