import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

/**
 * 用于统计项目内代码行数的小程序。
 * <p>
 * 支持文件类型过滤，注释过滤。
 * TODO: 支持正则过滤。
 * </p>
 * @author Yusen
 */
public class CodeLineCounter {
    private Map<String, Integer> counterMap = new HashMap<String, Integer>();
    //支持的文件类型
    private String[] supportSuffixList = new String[] {
        "java", "c", "cpp", "cc", "js", "py", "jsp", "html", "css", "php", "swift", "sql"
    };

    public CodeLineCounter() {
        Arrays.sort(supportSuffixList);
    }

    //使用DFS遍历目录树
    public void countWithDFS(String path, String[] filterList, boolean isCommentIncluded) {
        File file = new File(path);

        //分别对文件不存在、目录、普通文件三种情况做处理
        if (!file.exists()) {
            return;
        } else if (file.isDirectory()) {
            //获取目录下文件名字列表
            String[] children = file.list();

            //遍历子目录
            for (String child : children) {
                countWithDFS(file.getAbsolutePath() + File.separator + child, filterList, isCommentIncluded);
            }
        } else if (!filter(file.getName(), filterList)) {
            //计算代码行数
            int lineNum = getLineNum(file, isCommentIncluded);
            String suffix = getSuffix(file.getName());
            int num = 0;

            if (counterMap.containsKey(suffix)) {
                num = counterMap.get(suffix);
            }

            //更新map
            counterMap.put(suffix, num + lineNum);
        }
    }

    //打印结果
    public void print() {
        //对map进行排序
        ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<>(counterMap.entrySet());
        Collections.sort(entryList, (e1, e2) -> {
            return e2.getValue() - e1.getValue();
        });
        int total = 0;

        //计算总行数
        for (Map.Entry<String, Integer> entry : entryList) {
            total += entry.getValue();
        }

        System.out.println("Type\tLine\tPercent");

        //输出结果
        for (Map.Entry<String, Integer> entry : entryList) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue() + "\t" + getPercent(entry.getValue(), total));
        }

        System.out.println("Total:\t" + total + "\t100%");
    }

    //计算代码行数
    private int getLineNum(File file, boolean isCommentIncluded) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int num = 0;
            boolean flag = true;
            String line = br.readLine();

            while (line != null) {
                //不对注释过滤
                if (isCommentIncluded) {
                    num++;
                } else {
                    //不在注释范围内
                    if (flag) {
                        num++;
                    }

                    //对注释进行处理(这个处理还是比较粗糙的，以后可以考虑使用正则来处理)
                    if (line.contains("/*")) {
                        flag = false;
                        num--;
                    } else if (line.contains("*/")) {
                        flag = true;
                    } else if (line.contains("//")) {
                        num--;
                    }
                }

                line = br.readLine();
            }

            br.close();
            return num;
        } catch (Exception e) {
            return 0;
        }
    }

    //获取文件后缀名
    private String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    //对文件类型进行过滤
    private boolean filter(String fileName, String[] filterList) {
        String suffix = getSuffix(fileName);

        //使用二分搜索加速
        if (Arrays.binarySearch(supportSuffixList, suffix) < 0 || Arrays.binarySearch(filterList, suffix) >= 0) {
            return true;
        }

        return false;
    }

    //计算百分比
    private String getPercent(int num, int total) {
        double percent = (double)num / total * 100;
        return Integer.toString((int)percent) + "%";
    }

    public static void main(String[] args) {
        String initalPath = args[0];
        //判断是否进行注释过滤，默认为true，即不过滤
        boolean isCommentIncluded = args[args.length - 1].equals("0") ? false : true;
        //计算过滤列表长度
        int length = args[args.length - 1].equals("0") || args[args.length - 1].equals("1") ? args.length - 2 : args.length - 1;
        String[] filterList = new String[length];

        for (int i = 0;i < length;i++) {
            filterList[i] = args[i + 1];
        }

        //执行
        Arrays.sort(filterList);
        CodeLineCounter codeLineCounter = new CodeLineCounter();
        codeLineCounter.countWithDFS(initalPath, filterList, isCommentIncluded);
        codeLineCounter.print();
    }
}