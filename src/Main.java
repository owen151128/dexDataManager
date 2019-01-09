
import model.DexInfo;
import model.DexMethodIds;
import model.dex.IntHolder;
import util.DexMethodCountUtil;
import util.DexUtil;
import util.FileUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        if (argCheck(args)) {
            Main main = new Main();
            System.exit(main.run(args));
        } else {
            System.out.println("check the arguments!");
            System.exit(-1);
        }
    }

    public static boolean argCheck(String[] args) {
        if (args.length > 1 || args.length < 1) {
            return false;
        }

        for (String s : args) {
            if (s == null) {
                return false;
            }
        }

        return true;
    }

    public int run(String[] args) {
        Path apkPath = new File(args[0]).toPath();
        Path randomPath = FileUtil.getRandomExtractPathFromTarget(apkPath);

        List<Path> dexList = DexUtil.dexExtractFromApk(apkPath, randomPath);
        DexMethodIds ids = DexUtil.getMethodInfo(randomPath);

        for (Map.Entry<String, Integer> entry : ids.getDexList().entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        if (dexList == null) {
            return -1;
        }

        for (Path dex : dexList) {
            try {
                long start = System.currentTimeMillis();
                DexInfo info = new DexInfo(dex);
                info.load();
                DexMethodCountUtil u = DexMethodCountUtil.getInstance();
                u.init();
                u.caculateDexMethodCount(info);
                System.out.println(u.getAllMethodCount());

                for (Map.Entry<String, Integer> entry : u.getMethodCounter().entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                long end = System.currentTimeMillis();

                System.out.println("time : " + (end - start) / 1000.0 + "s");
                System.out.println("next======================");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileUtil.deleteFile(randomPath);

        return 0;
    }
}
