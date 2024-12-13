package com.psgenerator.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DirectoryTreeGenerator {

    public static String generateTree(String rootDir, String[] excludeList) {
        StringBuilder treeBuilder = new StringBuilder();
        File root = new File(rootDir);
        if (!root.exists() || !root.isDirectory()) {
            return "Invalid directory!";
        }
        generateTreeRecursively(root, "", treeBuilder, excludeList);
        return treeBuilder.toString();
    }

    private static void generateTreeRecursively(File folder, String indent, StringBuilder treeBuilder, String[] excludeList) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (int i = 0; i < files.length; i++) {
            boolean isLast = (i == files.length - 1);
            String prefix = isLast ? "└── " : "├── ";

            if (shouldExclude(files[i].getName(), excludeList)) continue;

            treeBuilder.append(indent).append(prefix).append(files[i].getName());
            if (files[i].isDirectory()) {
                treeBuilder.append("/\n");
                generateTreeRecursively(files[i], indent + (isLast ? "    " : "│   "), treeBuilder, excludeList);
            } else {
                treeBuilder.append("\n");
            }
        }
    }

    private static boolean shouldExclude(String name, String[] excludeList) {
        if (excludeList == null) return false;
        for (String exclude : excludeList) {
            if (name.equalsIgnoreCase(exclude.trim())) {
                return true;
            }
        }
        return false;
    }

    public static void saveTreeToFile(String tree, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(tree);
        }
    }
}
