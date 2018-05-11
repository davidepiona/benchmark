package com.benchmark.upload;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WSController {

    @MessageMapping("/echo")
    public boolean echo(String message) {
        return StringUtils.containsIgnoreCase(message, "hello");
    }

    @MessageMapping("/upload")
    public boolean upload(UploadRequest req) {

//        StringWriter w = getStringWriterByFileId();
        StringWriter w = new StringWriter();
        w.append(req.getFileContent());
        try {
            FileUtils.writeByteArrayToFile(new File("/home/davide/dev/benchmark/media/","ciccio"+req.getPartId()+".tmp"), Base64.getDecoder().decode(w.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }


//        PrintStream printStream = getUploadHandler(req.getId(), req.getPartId());
//        printStream.print(req.getFileContent());
        System.out.println("part: " + req.getPartId() + "  count: " + req.getPartCount());
        if (req.getPartId() == req.getPartCount()) {
//            IOUtils.closeQuietly(printStream);
            System.out.println("entrato");
            File dest = new File("/home/davide/dev/benchmark/media/", "ciccio0.tmp");
            for (int partId = 1; partId < req.getPartCount(); partId++) {
                byte[] buffer = null;
                try {
                    buffer = IOUtils.toByteArray(new File("/home/davide/dev/benchmark/media/", "ciccio" + partId + ".tmp").toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileUtils.writeByteArrayToFile(dest, buffer, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("finito");
            dest.renameTo(new File("/home/davide/dev/benchmark/media/","bubu.mp4"));

        }

        return req != null;
    }
//        System.err.println(req);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        byte[] buf = new byte[20480];
//        try {
//            for (int readNum; (readNum = req.getFileContent().read(buf)) != -1;) {
//                bos.write(buf, 0, readNum); //no doubt here is 0
//                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
//                System.out.println("read " + readNum + " bytes,");
//            }
//        } catch (IOException ex) {
////            Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        byte[] bytes = bos.toByteArray();
//
//        //below is the different part
//        File someFile = new File("java2.mp4");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(someFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            fos.write(bytes);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();

    private Map<String, PrintStream> manager = new HashMap<>();

    private Map<String, StringWriter> stringwriter = new HashMap<>();

    private PrintStream getUploadHandler(String id, int partId) {
        PrintStream result = manager.get(id);
        if (result == null) {
            PipedOutputStream out = new PipedOutputStream();
            PrintStream printStream = new PrintStream(out);

            new Thread(() -> {
                try (PipedInputStream in = new PipedInputStream(out)) {
                    try (InputStream bytesIn = Base64.getDecoder().wrap(in)) {
                        FileUtils.copyInputStreamToFile(bytesIn, new File("/home/davide/dev/benchmark/media/","ciccio"+partId+".tmp"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.err.println("TERMINATED");
            }, "FileWriter").start();
            manager.put(id, printStream);
            return printStream;
        }

        return result;
    }

    @MessageMapping("/test/{id}")
    public int test(@PathVariable int id) {
        System.err.println(id);
        return id;
    }

}
