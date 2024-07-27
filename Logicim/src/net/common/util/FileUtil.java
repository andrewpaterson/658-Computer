package net.common.util;

import net.common.SimulatorException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtil
{
  private static final int EOF = -1;

  public static File createTemporaryFile(String prefix, String suffix)
  {
    try
    {
      File tempFile = File.createTempFile(prefix, suffix);
      tempFile.deleteOnExit();
      return tempFile;
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static boolean makeDirectory(String directory)
  {
    if (!StringUtil.isEmptyOrNull(directory))
    {
      File file = new File(directory);
      return file.mkdirs();
    }
    return true;
  }

  public static boolean existOrMakeDirectory(String directory)
  {
    if (exists(directory))
    {
      return true;
    }

    return makeDirectory(directory);
  }

  public static List<File> findFiles(String directoryName, String extension)
  {
    if (!extension.startsWith("."))
    {
      extension = "." + extension;
    }

    List<File> files = new ArrayList<File>();

    File directory = new File(directoryName);
    if (directory.isDirectory())
    {
      findFiles(directory, extension, files);
    }
    return files;
  }

  public static List<File> findFiles(String directoryName)
  {
    File[] files = new File(directoryName).listFiles();
    if (files == null)
    {
      return new ArrayList<>();
    }

    return Arrays.asList(files);
  }

  private static void findFiles(File directory, String extension, List<File> fileList)
  {
    File[] files = directory.listFiles();
    for (File file : files)
    {
      if (file.isDirectory())
      {
        findFiles(file, extension, fileList);
      }
      else
      {
        if (file.getName().endsWith(extension))
        {
          fileList.add(file);
        }
      }
    }
  }

  public static List<File> findFilesContaining(String directoryName, String... contains)
  {
    List<File> files = new ArrayList<File>();

    File directory = new File(directoryName);
    if (directory.isDirectory())
    {
      findFilesContaining(directory, files, contains);
    }
    return files;
  }

  private static void findFilesContaining(File directory, List<File> fileList, String... contains)
  {
    File[] files = directory.listFiles();
    for (File file : files)
    {
      if (file.isDirectory())
      {
        findFilesContaining(file, fileList, contains);
      }
      else
      {
        boolean containsAll = true;
        for (String string : contains)
        {
          if (!file.getName().contains(string))
          {
            containsAll = false;
            break;
          }
        }
        if (containsAll)
        {
          fileList.add(file);
        }
      }
    }
  }

  public static boolean deleteDirectory(String directoryName)
  {
    File file = new File(directoryName);
    return deleteDirectory(file);
  }

  public static void deleteFile(String fileName)
  {
    boolean success = quietDeleteFile(fileName);

    if (!success || exists(fileName))
    {
      throw new SimulatorException("Could not delete file [%s]", fileName);
    }
  }

  public static void deleteFiles(String... fileNames)
  {
    for (String fileName : fileNames)
    {
      deleteFile(fileName);
    }
  }

  public static boolean quietDeleteFile(String fileName)
  {
    File file = new File(fileName);
    return file.delete();
  }

  public static void quietDeleteFiles(String... fileNames)
  {
    for (String fileName : fileNames)
    {
      quietDeleteFile(fileName);
    }
  }

  public static boolean renameFile(String fileName, String newFileName)
  {
    File file = new File(fileName);
    File newFile = new File(newFileName);
    return file.renameTo(newFile);
  }

  public static boolean exists(String parent, String fileName)
  {
    return new File(parent, fileName).exists();
  }

  public static boolean exists(String fileName)
  {
    return new File(fileName).exists();
  }

  public static long size(String path)
  {
    return new File(path).length();
  }

  private static boolean deleteDirectory(File dir)
  {
    if (dir.isDirectory())
    {
      String[] children = dir.list();
      for (String aChildren : children)
      {
        boolean success = deleteDirectory(new File(dir, aChildren));
        if (!success)
        {
          return false;
        }
      }
    }
    return dir.delete();
  }

  public static File createFile(String fileName)
  {
    File file = new File(fileName);
    try
    {
      if (!file.createNewFile())
      {
        return null;
      }
      return file;
    }
    catch (IOException e)
    {
      return null;
    }
  }

  public static File writeFile(String fileName, String contents)
  {
    File file = new File(fileName);
    writeFile(file, contents);
    return file;
  }

  public static void writeFile(File file, String contents)
  {
    try
    {
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(contents);
      fileWriter.close();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static void appendLine(File file, String contents)
  {
    try
    {
      FileWriter fileWriter = new FileWriter(file, true);
      fileWriter.write(contents);
      fileWriter.write('\n');
      fileWriter.close();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static void writeFile(File file, byte[] contents)
  {
    try
    {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(contents);
      fileOutputStream.close();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static List<String> readLines(String filename)
  {
    try
    {
      return readLines(new FileReader(filename));
    }
    catch (FileNotFoundException e)
    {
      return new ArrayList<String>();
    }
  }

  public static List<String> readLines(File file)
  {
    try
    {
      return readLines(new FileReader(file));
    }
    catch (FileNotFoundException e)
    {
      return new ArrayList<String>();
    }
  }

  public static File getClasspathFile(String filename)
  {
    ClassLoader classLoader = FileUtil.class.getClassLoader();
    return new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
  }

  public static Reader openClasspathReader(String filename)
  {
    InputStream inputStream = openClasspathInputStream(filename);
    return new InputStreamReader(inputStream);
  }

  public static InputStream openClasspathInputStream(String filename)
  {
    String resourceName;
    if (filename.startsWith("/"))
    {
      resourceName = filename.substring(1);
    }
    else
    {
      resourceName = filename;
    }

    InputStream result = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);

    if (result != null)
    {
      return result;
    }
    else
    {
      throw new SimulatorException("[%s] does not refer to a class path resource", filename);
    }
  }

  public static void touch(String touchFilename)
  {
    File file = new File(touchFilename);
    boolean success;
    try
    {
      success = file.createNewFile();
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }

    if (!success || !exists(touchFilename))
    {
      throw new SimulatorException("Could not touch file name [%s]", touchFilename);
    }
  }

  // This is slow Unix-Only solution so use carefully.

  public static String getWorkingDirectory()
  {
    return System.getProperty("user.dir");
  }

  public static String getPackagePath(Class aClass)
  {
    String packageName = aClass.getPackage().getName();
    return packageName.replace('.', '/');
  }

  public static String getBasename(String filePath)
  {
    return new File(filePath).getName();
  }

  public static String getDirectoryName(String filePath)
  {
    return new File(filePath).getParent();
  }

  public static String getFileName(String filePath)
  {
    return new File(filePath).getName();
  }

  public static Writer createWriter(String filename)
  {
    return createWriter(new File(filename));
  }

  public static Writer createWriter(File file)
  {
    try
    {
      return new BufferedWriter(new FileWriter(file));
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static BufferedOutputStream openOutputStream(File file)
  {
    try
    {
      return new BufferedOutputStream(new FileOutputStream(file));
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static BufferedInputStream openInputStream(File file)
  {
    try
    {
      return new BufferedInputStream(new FileInputStream(file));
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static BufferedReader openFile(String fileName)
  {
    try
    {
      FileReader reader = new FileReader(fileName);
      return new BufferedReader(reader);
    }
    catch (FileNotFoundException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static BufferedReader openFile(File file)
  {
    try
    {
      FileReader reader = new FileReader(file);
      return new BufferedReader(reader);
    }
    catch (FileNotFoundException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public static BufferedReader openFile(byte[] bytes)
  {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    InputStreamReader reader = new InputStreamReader(inputStream);
    return new BufferedReader(reader);
  }

  public static boolean isDirectory(String path)
  {
    return new File(path).isDirectory();
  }

  public static boolean isDirectory(File path)
  {
    return path.isDirectory();
  }

  public static InputStream openInputStream(byte[] bytes)
  {
    return new ByteArrayInputStream(bytes);
  }

  public static String getExtension(String name)
  {
    File file = new File(name);
    String fileName = file.getName();
    int index = fileName.lastIndexOf('.');
    if (index != -1)
    {
      return fileName.substring(index + 1);
    }
    else
    {
      return "";
    }
  }

  public static String removeExtension(String filePath, String ext)
  {
    if (filePath == null)
    {
      return null;
    }
    if (!ext.startsWith("."))
    {
      ext = "." + ext;
    }

    int index = filePath.lastIndexOf(ext);
    if (index == -1)
    {
      return filePath;
    }
    return filePath.substring(0, index);
  }

  public static String removeExtension(String filePath)
  {
    if (filePath == null)
    {
      return null;
    }

    int index = filePath.lastIndexOf('.');
    if (index == -1)
    {
      return filePath;
    }
    return filePath.substring(0, index);
  }

  public static void sort(List<File> files)
  {
    Collections.sort(files);
  }

  public static BufferedWriter openWriter(String filename, boolean append)
  {
    try
    {
      Writer stream = new FileWriter(filename, append);
      return new BufferedWriter(stream);
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  private static boolean containsIllegalCharacters(String fileName)
  {
    List<Character> illegalCharacters = getIllegalFileCharacters();
    for (int i = 0; i < fileName.length(); i++)
    {
      char c = fileName.charAt(i);
      if (illegalCharacters.contains(c))
      {
        return true;
      }
    }

    return false;
  }

  private static List<Character> getIllegalFileCharacters()
  {
    return CollectionUtil.newList('!', '"', '#', '$', '&', '\'', '(', ')', '*', ',', ';', '<', '=', '>', '?', '[', ']', '^', '`', '{', '|', '}', '~', ' ', '\t', '\\');
  }

  public static String cleanFileName(String fileName)
  {
    boolean containsExclamation = fileName.contains("!");
    if ((fileName.startsWith("\"")) && (fileName.endsWith("\"")) && !containsExclamation)
    {
      return fileName;
    }
    if (!containsIllegalCharacters(fileName))
    {
      return fileName;
    }
    if (!fileName.contains("\"") && !containsExclamation)
    {
      return "\"" + fileName + "\"";
    }
    return escapeIllegalCharacters(fileName);
  }

  private static String escapeIllegalCharacters(String fileName)
  {
    StringBuilder builder = new StringBuilder();

    List<Character> illegalCharacters = getIllegalFileCharacters();
    for (int i = 0; i < fileName.length(); i++)
    {
      char c = fileName.charAt(i);
      if (illegalCharacters.contains(c))
      {
        builder.append('\\');
      }
      builder.append(c);
    }
    return builder.toString();
  }

  public static String normaliseDirectoryPath(String path)
  {
    if (path.endsWith("/"))
    {
      return path;
    }
    else
    {
      return path + "/";
    }
  }

  private static List<String> readLines(Reader input)
  {
    BufferedReader bufReader = toBufferedReader(input);
    try
    {
      List<String> list = new ArrayList<>();
      String line;
      while ((line = bufReader.readLine()) != null)
      {
        list.add(line);
      }
      return list;
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    finally
    {
      closeQuietly(input);
    }
  }

  public static void closeQuietly(Closeable closeable)
  {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      }
      catch (IOException e)
      {
      }
    }
  }

  public static BufferedReader toBufferedReader(final Reader reader)
  {
    return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
  }

  public static List<String> bufferedReadLines(Reader reader)
  {
    BufferedReader bufReader = toBufferedReader(reader);
    try
    {
      List<String> list = new ArrayList<>();
      String line;
      while ((line = bufReader.readLine()) != null)
      {
        list.add(line);
      }
      return list;
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    finally
    {
      closeQuietly(reader);
    }
  }

  public static void writeFile(byte[] data, String fullFileName, boolean append)
  {
    existOrMakeDirectory(getDirectoryName(fullFileName));

    OutputStream out = null;
    try
    {
      out = new FileOutputStream(fullFileName, append);
      out.write(data);
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    finally
    {
      try
      {
        if (out != null)
        {
          out.close();
        }
      }
      catch (IOException ignored)
      {
      }
    }
  }

  public static String joinFileNameSegments(String... segments)
  {
    StringBuilder result = new StringBuilder();

    if (segments.length == 0)
    {
      return result.toString();
    }

    result.append(segments[0]);

    for (int i = 1; i < segments.length; i++)
    {
      String path = segments[i];
      if (!result.toString().endsWith("/"))
      {
        result.append("/");
      }
      result.append(path);
    }

    return result.toString();
  }

  public static List<String> createPathList(String[] pathComponents)
  {
    List<String> result = new ArrayList<>();

    for (String pathComponent : pathComponents)
    {
      if (!pathComponent.trim().isEmpty())
      {
        result.add(pathComponent);
      }
    }

    return result;
  }

  public static List<String> getPathAsList(File file)
  {
    File parentFile = file.getParentFile();
    if (parentFile != null)
    {
      String path = parentFile.getPath();
      String[] pathComponents = path.split(Pattern.quote(File.separator));

      return createPathList(pathComponents);
    }
    else
    {
      return new ArrayList<>();
    }
  }

  public static List<String> bufferedReadLines(Reader reader, int numberOfLinesFromStart)
  {
    BufferedReader bufReader = toBufferedReader(reader);
    try
    {
      List<String> list = new ArrayList<>();
      String line;
      while ((line = bufReader.readLine()) != null)
      {
        list.add(line);
      }
      return list;
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    finally
    {
      closeQuietly(reader);
    }
  }

  private static List<String> readStartLines(Reader input, int numberOfLinesFromStart)
  {
    BufferedReader bufReader = toBufferedReader(input);
    int count = 0;
    try
    {
      List<String> list = new ArrayList<>();
      String line;
      while ((line = bufReader.readLine()) != null)
      {
        list.add(line);
        count++;
        if (count >= numberOfLinesFromStart)
        {
          break;
        }
      }
      return list;
    }
    catch (IOException e)
    {
      throw new SimulatorException(e.getMessage());
    }
    finally
    {
      closeQuietly(input);
    }
  }

  public static List<String> readStartLines(File file, int numberOfLinesFromStart)
  {
    try
    {
      return readStartLines(new FileReader(file), numberOfLinesFromStart);
    }
    catch (FileNotFoundException e)
    {
      return new ArrayList<String>();
    }
  }

  public static File removeExtension(File file)
  {
    String name = file.getName();
    String path = file.getPath();
    String noExtension = removeExtension(name);
    path = path.substring(0, path.length() - name.length());
    path = path + noExtension;
    return new File(path);
  }

  public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
      throws IOException
  {
    long count = 0;
    int n;
    while (EOF != (n = input.read(buffer)))
    {
      output.write(buffer, 0, n);
      count += n;
    }
    return count;
  }

  public static int copy(final InputStream input, final OutputStream output) throws IOException
  {
    final long count = copyLarge(input, output, new byte[4 * 1024]);
    if (count > Integer.MAX_VALUE)
    {
      return -1;
    }
    return (int) count;
  }

  public static byte[] toByteArray(final InputStream input) throws IOException
  {
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    int copy = copy(input, output);
    return output.toByteArray();
  }

  public static byte[] readBytes(File file)
  {
    try
    {
      BufferedInputStream inputStream = openInputStream(file);
      return toByteArray(inputStream);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public static String readFile(File file)
  {
    StringBuilder builder = new StringBuilder();
    List<String> strings = readLines(file);
    for (String string : strings)
    {
      builder.append(string + "\n");
    }
    return builder.toString();
  }
}

