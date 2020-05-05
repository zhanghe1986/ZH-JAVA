package com.hankcs.hanlp.traininfer;


import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.corpus.document.sentence.word.CompoundWord;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.model.perceptron.*;
import com.hankcs.hanlp.seg.common.Term;

import java.io.*;
import java.util.List;

public class ModelInfer {

    public static void main(String[] args)
    {
        try {
            Long beginTime = System.currentTimeMillis();
            File filename = null;
            InputStreamReader reader = null;
            BufferedReader br = null;

            File fileBIO = null;
            BufferedWriter out = null;

            File fileWord = null;
            BufferedWriter outWord = null;

            PerceptronLexicalAnalyzer segmenter = new PerceptronLexicalAnalyzer(com.hankcs.hanlp.traininfer.Config.CWS_MODEL_FILE,
                com.hankcs.hanlp.traininfer.Config.POS_MODEL_FILE, Config.NER_MODEL_FILE);

            try {
                String pathname = "C:\\Users\\lenovo\\Desktop\\testing\\input.txt";
                filename = new File(pathname);
                reader = new InputStreamReader(new FileInputStream(filename));
                br = new BufferedReader(reader);

                fileBIO = new File("C:\\Users\\lenovo\\Desktop\\testing\\bio_output.txt");
                fileBIO.createNewFile();
                out = new BufferedWriter(new FileWriter(fileBIO));

                fileWord = new File("C:\\Users\\lenovo\\Desktop\\testing\\word_output.txt");
                fileWord.createNewFile();
                outWord = new BufferedWriter(new FileWriter(fileWord));

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    List<Term> termList = segmenter.seg(line);

                    for (Term tempTerm : termList) {
                        String tempWord = tempTerm.word.trim();
                        if (tempWord.isEmpty()) {
                            continue;
                        }
                        if ("nr".equals(tempTerm.nature.toString())) {
                            sb.append(get_NR(tempWord));
                        }else if ("ns".equals(tempTerm.nature.toString())) {
                            sb.append(get_NS(tempWord));
                        }else if ("nt".equals(tempTerm.nature.toString())) {
                            sb.append(get_NT(tempWord));
                        }else {
                            sb.append(get_O(tempWord));
                        }

                        outWord.write(tempTerm + "\t");
                    }
                    out.write(sb.toString() + "\n");
                    outWord.write("\n");
                }
                Long endTime = System.currentTimeMillis();
                Long durationTime = endTime - beginTime;
                System.out.println(durationTime + "ms");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
                br.close();
                out.close();
                outWord.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取人名
     * */
    public static String get_NR(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (i==0) {
                sb.append(word.charAt(i))
                    .append(" B-PER\n");
            } else {
                sb.append(word.charAt(i))
                    .append(" I-PER\n");
            }
        }
        return sb.toString();
    }

    /**
     * 获取地点
     * */
    public static String get_NS(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (i==0) {
                sb.append(word.charAt(i))
                    .append(" B-LOC\n");
            } else {
                sb.append(word.charAt(i))
                    .append(" I-LOC\n");
            }
        }
        return sb.toString();
    }

    /**
     * 获取机构
     * */
    public static String get_NT(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (i==0) {
                sb.append(word.charAt(i))
                    .append(" B-ORG\n");
            } else {
                sb.append(word.charAt(i))
                    .append(" I-ORG\n");
            }
        }
        return sb.toString();
    }

    public static String get_O(String word) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            sb.append(word.charAt(i))
                .append(" O\n");
        }
        return sb.toString();
    }
}
