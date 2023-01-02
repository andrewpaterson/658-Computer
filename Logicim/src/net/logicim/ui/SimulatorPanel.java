package net.logicim.ui;

import net.logicim.common.util.FileUtil;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.file.reader.LogicimFileReader;
import net.logicim.file.writer.LogicimFileWriter;
import net.logicim.ui.components.typeeditor.FamilyPropertyEditorFactory;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;
import net.logicim.ui.error.ErrorFrame;
import net.logicim.ui.input.event.*;
import net.logicim.ui.integratedcircuit.extra.OscilloscopeViewFactory;
import net.logicim.ui.integratedcircuit.factory.ViewFactoryStore;
import net.logicim.ui.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.and.AndGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.and.NandGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.BufferViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.InverterViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.or.NorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.or.OrGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XnorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.power.GroundViewFactory;
import net.logicim.ui.integratedcircuit.standard.power.PositivePowerViewFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

import static net.logicim.domain.common.Units.nS_IN_mS;

public class SimulatorPanel
    extends JPanel
{
  protected Image backBufferImage;
  protected Graphics2D backBuffer;
  protected boolean running;
  protected long period;

  protected SimulatorEditor simulatorEditor;
  protected JFrame frame;

  public SimulatorPanel(JFrame frame)
  {
    this.frame = frame;
    running = false;
    period = 16 * nS_IN_mS;

    simulatorEditor = new SimulatorEditor(this);
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
                                          new PositivePowerViewFactory());

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
    if ((backBufferImage != null) && (backBuffer != null))
    {
      simulatorEditor.paint(backBuffer);
    }
  }

  private boolean update(long beforeTime, long overtime, int tickCount)
  {
    boolean done = simulatorEditor.tick(tickCount);

    long afterTime = System.nanoTime();
    long timeDiff = afterTime - beforeTime;
    long sleepTime = (period - timeDiff) - overtime;
    return (sleepTime > 0) || done;
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
    backBuffer = (Graphics2D) backBufferImage.getGraphics();

    simulatorEditor.addInputEvent(new ResizedEvent(width, height));
  }

  public void mousePressed(int x, int y, int button)
  {
    simulatorEditor.addInputEvent(new MousePressedEvent(x, y, button));
  }

  public void mouseReleased(int x, int y, int button)
  {
    simulatorEditor.addInputEvent(new MouseReleasedEvent(x, y, button));
  }

  public void mouseMoved(int x, int y)
  {
    simulatorEditor.addInputEvent(new MouseMovedEvent(x, y));
  }

  public void windowClosing()
  {
    simulatorEditor.addInputEvent(new WindowClosingEvent());
  }

  public void mouseExited()
  {
    simulatorEditor.addInputEvent(new MouseExitedEvent());
  }

  public void mouseEntered(int x, int y)
  {
    simulatorEditor.addInputEvent(new MouseEnteredEvent(x, y));
  }

  public void mouseWheel(int wheelRotation)
  {
    simulatorEditor.addInputEvent(new MouseWheelEvent(wheelRotation));
  }

  public void keyPressed(int keyCode)
  {
    simulatorEditor.addInputEvent(new KeyPressedEvent(keyCode));
  }

  public void keyReleased(int keyCode)
  {
    simulatorEditor.addInputEvent(new KeyReleasedEvent(keyCode));
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
        CircuitData savedData = simulatorEditor.save();

        try
        {
          new LogicimFileWriter().writeXML(savedData, newFile);
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
        CircuitData savedData = new LogicimFileReader().load(file);
        try
        {
          simulatorEditor.load(savedData);
        }
        catch (RuntimeException exception)
        {
          exception.printStackTrace();
          ErrorFrame.createWindow(frame, exception);
        }
      }
    }
    simulatorEditor.clearButtons();
  }

  public JFrame getFrame()
  {
    return frame;
  }
}

