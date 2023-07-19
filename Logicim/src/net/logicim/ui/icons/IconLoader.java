package net.logicim.ui.icons;

import net.logicim.common.SimulatorException;
import net.logicim.common.util.FileUtil;
import net.logicim.ui.common.Colours;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IconLoader
{
  public static final String ABOUT = "about_24.png";
  public static final String ADD_COMPONENT = "add_component_24.png";
  public static final String ADD_TUNNEL = "add_glabel_24.png";
  public static final String ADD_CONNECTION = "add_junction_24.png";
  public static final String ADD_LABEL = "add_label_24.png";
  public static final String ADD_SPLITTER = "add_line2bus_24.png";
  public static final String ADD_TRACE = "add_line_24.png";
  public static final String ADD_GND = "add_power_24.png";
  public static final String ADD_SUBCIRCUIT = "module_24.png";
  public static final String NEW_SUBCIRCUIT = "new_footprint_24.png";
  public static final String BREAK_TRACE = "break_line_24.png";
  public static final String CANCEL = "cancel_24.png";
  public static final String TICK = "checked_ok_24.png";
  public static final String SETTINGS = "config_24.png";
  public static final String COPY = "copy_24.png";
  public static final String CURSOR = "cursor_24.png";
  //public static final String = "cursor_shape_24.png";
  public static final String CUT = "cut_24.png";
  public static final String LOAD = "directory_open_24.png";
  public static final String DUPLICATE = "duplicate_24.png";
  public static final String EDIT = "edit_24.png";
  public static final String ENTER_SUBCIRCUIT = "enter_sheet_24.png";
  public static final String ERROR = "ercerr_24.png";
  public static final String WARNING = "ercwarn_24.png";
  //public static final String = "exchange_24.png";
  public static final String EXIT = "exit_24.png";
  public static final String EXPORT_TO_KICAD = "export_file_24.png";
  public static final String FILTER = "filter_24.png";
  public static final String SEARCH = "find_24.png";
  //public static final String = "flag_24.png";
  public static final String QUESTION = "help_24.png";
  //public static final String = "import_24.png";
  //public static final String = "locked_24.png";
  //public static final String = "lock_unlock_24.png";
  public static final String MEASURE = "measurement_24.png";
  public static final String MIRROR_HORIZONTAL = "mirror_h_24.png";
  public static final String MIRROR_VERTICAL = "mirror_v_24.png";
  public static final String NEW = "new_generic_24.png";
  public static final String OPTIONS = "options_generic_24.png";
  public static final String REFRESH = "refresh_24.png";
  public static final String ROTATE_LEFT = "rotate_ccw_24.png";
  public static final String ROTATE_RIGHT = "rotate_cw_24.png";
  public static final String SAVE = "save_24.png";
  public static final String SIMULATION_RUN = "sim_run_24.png";
  public static final String SIMULATION_PAUSE = "sim_stop_24.png";
  public static final String SIMULATION_DEFAULT = "sim_default_24.png";
  public static final String SIMULATION_FASTER = "sim_faster_24.png";
  public static final String SIMULATION_SLOWER = "sim_slower_24.png";
  public static final String SIMULATION_RESET = "sim_reset_24.png";
  public static final String TEXT = "text_24.png";
  public static final String DELETE = "trash_24.png";
  public static final String UNDO = "undo_24.png";
  public static final String REDO = "redo_24.png";
  //public static final String = "unlocked_24.png";
  public static final String ZOOM_TO_DEFAULT = "zoom_center_on_screen_24.png";
  public static final String ZOOM_FIT_ALL = "zoom_fit_in_page_24.png";
  public static final String ZOOM_IN = "zoom_in_24.png";
  public static final String ZOOM_OUT = "zoom_out_24.png";
  public static final String ZOOM_FIT_SELECTION = "zoom_selection_24.png";
  public static final String POINT_VIEW = "grid_24.png";
  public static final String VIEW_SIMULATION_TREE = "simulation_tree_24.png";
  public static final String SIMULATION_SELECT = "array_24.png";
  public static final String SIMULATION_NEW = "new_simulation_24.png";

  private static IconLoader instance = null;

  private Map<String, Icon> normalIcons;
  private Map<String, Icon> pressedIcons;
  private Map<String, Icon> rolloverIcons;
  private Map<String, Icon> disabledIcons;

  public IconLoader()
  {
    String iconPath = getWorkingDirectory().getAbsolutePath() + "/res/icons";
    List<File> files = FileUtil.findFiles(iconPath, "png");
    Map<String, File> imageFileMap = new LinkedHashMap<>();
    for (File file : files)
    {
      imageFileMap.put(file.getName(), file);
    }

    normalIcons = new LinkedHashMap<>();
    pressedIcons = new LinkedHashMap<>();
    rolloverIcons = new LinkedHashMap<>();
    disabledIcons = new LinkedHashMap<>();

    createIcons(imageFileMap, ABOUT);
    createIcons(imageFileMap, ADD_COMPONENT);
    createIcons(imageFileMap, ADD_TUNNEL);
    createIcons(imageFileMap, ADD_CONNECTION);
    createIcons(imageFileMap, ADD_LABEL);
    createIcons(imageFileMap, ADD_SPLITTER);
    createIcons(imageFileMap, ADD_TRACE);
    createIcons(imageFileMap, ADD_GND);
    createIcons(imageFileMap, ADD_SUBCIRCUIT);
    createIcons(imageFileMap, NEW_SUBCIRCUIT);
    createIcons(imageFileMap, BREAK_TRACE);
    createIcons(imageFileMap, CANCEL);
    createIcons(imageFileMap, TICK);
    createIcons(imageFileMap, SETTINGS);
    createIcons(imageFileMap, COPY);
    createIcons(imageFileMap, CURSOR);
    createIcons(imageFileMap, CUT);
    createIcons(imageFileMap, LOAD);
    createIcons(imageFileMap, DUPLICATE);
    createIcons(imageFileMap, EDIT);
    createIcons(imageFileMap, ENTER_SUBCIRCUIT);
    createIcons(imageFileMap, ERROR);
    createIcons(imageFileMap, WARNING);
    createIcons(imageFileMap, EXIT);
    createIcons(imageFileMap, EXPORT_TO_KICAD);
    createIcons(imageFileMap, FILTER);
    createIcons(imageFileMap, SEARCH);
    createIcons(imageFileMap, QUESTION);
    createIcons(imageFileMap, MEASURE);
    createIcons(imageFileMap, MIRROR_HORIZONTAL);
    createIcons(imageFileMap, MIRROR_VERTICAL);
    createIcons(imageFileMap, NEW);
    createIcons(imageFileMap, OPTIONS);
    createIcons(imageFileMap, REFRESH);
    createIcons(imageFileMap, ROTATE_LEFT);
    createIcons(imageFileMap, ROTATE_RIGHT);
    createIcons(imageFileMap, SAVE);
    createIcons(imageFileMap, SIMULATION_RUN);
    createIcons(imageFileMap, SIMULATION_PAUSE);
    createIcons(imageFileMap, SIMULATION_DEFAULT);
    createIcons(imageFileMap, SIMULATION_FASTER);
    createIcons(imageFileMap, SIMULATION_SLOWER);
    createIcons(imageFileMap, SIMULATION_RESET);
    createIcons(imageFileMap, TEXT);
    createIcons(imageFileMap, DELETE);
    createIcons(imageFileMap, UNDO);
    createIcons(imageFileMap, REDO);
    createIcons(imageFileMap, ZOOM_TO_DEFAULT);
    createIcons(imageFileMap, ZOOM_FIT_ALL);
    createIcons(imageFileMap, ZOOM_IN);
    createIcons(imageFileMap, ZOOM_OUT);
    createIcons(imageFileMap, ZOOM_FIT_SELECTION);
    createIcons(imageFileMap, POINT_VIEW);
    createIcons(imageFileMap, VIEW_SIMULATION_TREE);
    createIcons(imageFileMap, SIMULATION_SELECT);
    createIcons(imageFileMap, SIMULATION_NEW);
  }

  public static Icon getIcon(String key)
  {
    return getInstance().normalIcons.get(key);
  }

  public static Icon getPressedIcon(String key)
  {
    return getInstance().pressedIcons.get(key);
  }

  public static Icon getDisabledIcon(String key)
  {
    return getInstance().disabledIcons.get(key);
  }

  public static Icon getRolloverIcon(String key)
  {
    return getInstance().rolloverIcons.get(key);
  }

  private GraphicsConfiguration getGraphicsConfiguration()
  {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
  }

  private BufferedImage convertToBufferedImage(BufferedImage image,
                                               int destWidth,
                                               int destHeight,
                                               Color background,
                                               Color border,
                                               int transparency)
  {
    int sourceWidth = image.getWidth();
    int sourceHeight = image.getHeight();
    int destX = (destWidth - sourceWidth) / 2;
    int destY = (destHeight - sourceHeight) / 2;

    BufferedImage copy = createBufferedImage(destWidth, destHeight, transparency);

    Graphics2D graphics = copy.createGraphics();
    if (background != null)
    {
      graphics.setColor(background);
      graphics.fillRect(0, 0, destWidth, destHeight);
    }
    if (border != null)
    {
      graphics.setColor(border);
      graphics.drawRect(0, 0, destWidth, destHeight);
    }
    graphics.drawImage(image,
                       destX,
                       destY,
                       destX + sourceWidth,
                       destY + sourceHeight,
                       0,
                       0,
                       sourceWidth,
                       sourceHeight,
                       null);
    graphics.dispose();
    return copy;
  }

  private BufferedImage createBufferedImage(int width, int height, int transparency)
  {
    GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
    return graphicsConfiguration.createCompatibleImage(width, height, transparency);
  }

  protected void createIcons(Map<String, File> imageFileMap, String key)
  {
    BufferedImage image = readImage(imageFileMap, key);

    BufferedImage rolloverImage = convertToBufferedImage(image,
                                                         32,
                                                         32,
                                                         getButtonRolloverBackground(),
                                                         getButtonRolloverBorder(), Transparency.OPAQUE);
    BufferedImage pressedImage = convertToBufferedImage(image,
                                                        32,
                                                        32,
                                                        getButtonPressedBackground(),
                                                        getButtonPressedBorder(), Transparency.OPAQUE);
    BufferedImage normalImage = convertToBufferedImage(image,
                                                       32,
                                                       32,
                                                       null,
                                                       null, Transparency.TRANSLUCENT);
    BufferedImage disabledImage = convertToBufferedImage(image,
                                                         32,
                                                         32,
                                                         null,
                                                         null, Transparency.TRANSLUCENT);
    Image disabledImage2 = GrayFilter.createDisabledImage(disabledImage);
    Icon rolloverIcon = new ImageIcon(rolloverImage);
    Icon pressedIcon = new ImageIcon(pressedImage);
    Icon normalIcon = new ImageIcon(normalImage);
    Icon disabledIcon = new ImageIcon(disabledImage2);

    normalIcons.put(key, normalIcon);
    pressedIcons.put(key, pressedIcon);
    rolloverIcons.put(key, rolloverIcon);
    disabledIcons.put(key, disabledIcon);
  }

  private Color getButtonPressedBorder()
  {
    return Colours.getInstance().getButtonPressedBorder();
  }

  private Color getButtonPressedBackground()
  {
    return Colours.getInstance().getButtonPressedBackground();
  }

  private Color getButtonRolloverBorder()
  {
    return Colours.getInstance().getButtonRolloverBorder();
  }

  private Color getButtonRolloverBackground()
  {
    return Colours.getInstance().getButtonRolloverBackground();
  }

  private BufferedImage readImage(Map<String, File> imageFileMap, String key)
  {
    File file = imageFileMap.get(key);
    if (file != null)
    {
      try
      {
        return ImageIO.read(file);
      }
      catch (IOException e)
      {
        throw new SimulatorException(e.getMessage());
      }
    }
    else
    {
      throw new SimulatorException("Cannot find icon [%s]", key);
    }
  }

  private File getWorkingDirectory()
  {
    return new File("").getAbsoluteFile();
  }

  public static IconLoader getInstance()
  {
    if (instance == null)
    {
      instance = new IconLoader();
    }
    return instance;
  }
}

