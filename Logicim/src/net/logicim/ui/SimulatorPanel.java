package net.logicim.ui;

import net.logicim.common.util.FileUtil;
import net.logicim.data.editor.EditorData;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.file.writer.LogicimFileWriter;
import net.logicim.ui.circuit.SubcircuitInstanceViewFactory;
import net.logicim.ui.common.Fonts;
import net.logicim.ui.common.wire.TunnelViewFactory;
import net.logicim.ui.components.typeeditor.FamilyPropertyEditorFactory;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;
import net.logicim.ui.error.ErrorFrame;
import net.logicim.ui.input.event.MouseWheelEvent;
import net.logicim.ui.input.event.*;
import net.logicim.ui.simulation.component.decorative.label.LabelViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;
import net.logicim.ui.simulation.component.integratedcircuit.extra.OscilloscopeViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.AndGateViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.InverterViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.NorGateViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.OrGateViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XnorGateViewFactory;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XorGateViewFactory;
import net.logicim.ui.simulation.component.passive.pin.PinViewFactory;
import net.logicim.ui.simulation.component.passive.power.GroundViewFactory;
import net.logicim.ui.simulation.component.passive.power.PositivePowerViewFactory;
import net.logicim.ui.simulation.component.passive.splitter.SplitterViewFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static net.logicim.domain.common.Units.nS_IN_mS;
import static net.logicim.file.writer.ReflectiveWriter.EDITOR_DATA_TAG_NAME;
import static net.logicim.file.writer.ReflectiveWriter.LOGICIM_DOC_NAME;

public class SimulatorPanel
    extends JPanel
    implements MouseListener,
               ComponentListener,
               MouseMotionListener,
               MouseWheelListener
{
  protected Image backBufferImage;
  protected Graphics2D graphics;
  protected boolean running;
  protected long period;

  protected Logicim logicim;
  protected JFrame frame;

  public SimulatorPanel(JFrame frame)
  {
    this.frame = frame;
    running = false;
    period = 16 * nS_IN_mS;

    logicim = new Logicim(this);
    FamilyVoltageConfigurationStore.getInstance();

    TypeEditorFactory.getInstance().addAll(new FamilyPropertyEditorFactory());

    ViewFactoryStore.getInstance().addAll(new ClockViewFactory(),
                                          new InverterViewFactory(),
                                          new OrGateViewFactory(),
                                          new NorGateViewFactory(),
                                          new AndGateViewFactory(),
                                          new NandGateViewFactory(),
                                          new XorGateViewFactory(),
                                          new XnorGateViewFactory(),
                                          new BufferViewFactory(),
                                          new OscilloscopeViewFactory(),
                                          new GroundViewFactory(),
                                          new PositivePowerViewFactory(),
                                          new SplitterViewFactory(),
                                          new TunnelViewFactory(),
                                          new LabelViewFactory(),
                                          new PinViewFactory(),
                                          new SubcircuitInstanceViewFactory());

    addComponentListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    addMouseWheelListener(this);
  }

  public void loop()
  {
    running = true;
    long overtime = 0;
    while (running)
    {
      long startTime = System.nanoTime();

      int tickCount = 0;
      while (!update(startTime, overtime, tickCount))
      {
        tickCount++;
      }

      render();
      paint();

      overtime = sleep(startTime, overtime);
    }
  }

  private void paint()
  {
    Graphics g = this.getGraphics();
    if ((g != null) && (backBufferImage != null))
    {
      g.drawImage(backBufferImage, 0, 0, null);
      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
  }

  private void render()
  {
    if ((backBufferImage != null) && (graphics != null))
    {
      logicim.paint(graphics);
    }
  }

  private boolean update(long beforeTime, long overtime, int tickCount)
  {
    boolean done = true;
    try
    {
      done = logicim.tick(tickCount);
    }
    catch (RuntimeException e)
    {
      handleException(e);
    }

    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    return (sleepTime > 0) || done;
  }

  public void handleException(RuntimeException e)
  {
    logicim.running = false;
    e.printStackTrace();

    logicim.undo();

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        ErrorFrame.createWindow(frame, e);
      }
    });
  }

  private long sleep(long beforeTime, long overtime)
  {
    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    if (sleepTime > 0)
    {
      try
      {
        Thread.sleep(sleepTime / nS_IN_mS);
      }
      catch (InterruptedException ignored)
      {
      }
      overtime = (System.nanoTime() - afterTime) - sleepTime;
    }
    else
    {
      Thread.yield();
      overtime = 0;
    }
    return overtime;
  }

  public void componentResized(int width, int height)
  {
    backBufferImage = createImage(width, height);
    graphics = (Graphics2D) backBufferImage.getGraphics();
    Fonts.getInstance().ensureDefaultFont(graphics);

    logicim.addEditorEvent(new ResizedEvent(width, height));
  }

  public void mousePressed(int x, int y, int button, int clickCount)
  {
    logicim.addEditorEvent(new MousePressedEvent(x, y, button, clickCount));
  }

  public void mouseReleased(int x, int y, int button)
  {
    logicim.addEditorEvent(new MouseReleasedEvent(x, y, button));
  }

  public void mouseMoved(int x, int y)
  {
    logicim.addEditorEvent(new MouseMovedEvent(x, y));
  }

  public void windowClosing()
  {
    logicim.addEditorEvent(new WindowClosingEvent());
  }

  public void mouseExited()
  {
    logicim.addEditorEvent(new MouseExitedEvent());
  }

  public void mouseEntered(int x, int y)
  {
    logicim.addEditorEvent(new MouseEnteredEvent(x, y));
  }

  public void mouseWheel(int wheelRotation)
  {
    logicim.addEditorEvent(new MouseWheelEvent(wheelRotation));
  }

  public void keyPressed(KeyEvent keyEvent)
  {
    logicim.addEditorEvent(new KeyPressedEvent(keyEvent.getKeyCode(),
                                               keyEvent.isControlDown(),
                                               keyEvent.isAltDown(),
                                               keyEvent.isShiftDown(),
                                               keyEvent.isMetaDown()));
  }

  public void keyReleased(KeyEvent keyEvent)
  {
    logicim.addEditorEvent(new KeyReleasedEvent(keyEvent.getKeyCode(),
                                                keyEvent.isControlDown(),
                                                keyEvent.isAltDown(),
                                                keyEvent.isShiftDown(),
                                                keyEvent.isMetaDown()));
  }

  public void saveSimulation()
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save circuit simulation");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicim files (*.logic)", "logic");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showSaveDialog(frame);
    if (userSelection == JFileChooser.APPROVE_OPTION)
    {
      File file = fileChooser.getSelectedFile();
      if (file != null)
      {
        File newFile = new File(FileUtil.removeExtension(file).getPath() + "." + "logic");
        EditorData savedData = logicim.save();

        try
        {
          LogicimFileWriter.writeXML(savedData, newFile);
        }
        catch (RuntimeException exception)
        {
          exception.printStackTrace();
          ErrorFrame.createWindow(frame, exception);
        }
      }
    }
  }

  public void loadSimulation()
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Load circuit simulation");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicim files (*.logic)", "logic");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showOpenDialog(frame);
    if (userSelection == JFileChooser.APPROVE_OPTION)
    {
      File file = fileChooser.getSelectedFile();
      if (file != null)
      {
        EditorData savedData = (EditorData) new LogicimFileReader(LOGICIM_DOC_NAME, EDITOR_DATA_TAG_NAME).load(file);
        try
        {
          logicim.loadFile(savedData);
        }
        catch (RuntimeException exception)
        {
          exception.printStackTrace();
          ErrorFrame.createWindow(frame, exception);
        }
      }
    }
    logicim.clearButtons();
  }

  public JFrame getFrame()
  {
    return frame;
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {

  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    mousePressed(e.getX(), e.getY(), e.getButton(), e.getClickCount());
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    mouseReleased(e.getX(), e.getY(), e.getButton());
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    mouseEntered(e.getX(), e.getY());
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    mouseExited();
  }

  @Override
  public void componentResized(ComponentEvent e)
  {
    componentResized(e.getComponent().getWidth(), e.getComponent().getHeight());
  }

  @Override
  public void componentMoved(ComponentEvent e)
  {
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
  }

  @Override
  public void componentHidden(ComponentEvent e)
  {
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    mouseMoved(e.getX(), e.getY());
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    mouseMoved(e.getX(), e.getY());
  }

  @Override
  public void mouseWheelMoved(java.awt.event.MouseWheelEvent e)
  {
    mouseWheel(e.getWheelRotation());
  }
}

