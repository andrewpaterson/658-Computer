package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;

public class EditParameters
    extends SimulatorEditorAction
{
  protected JFrame frame;

  public EditParameters(SimulatorEditor editor, JFrame frame)
  {
    super(editor);
    this.frame = frame;
  }

  @Override
  public void execute()
  {
    DiscreteView discreteView = editor.getHoverDiscreteView();

    Point mousePosition = MouseInfo.getPointerInfo().getLocation();

    if (discreteView != null)
    {
      JDialog dialog = new JDialog(frame, discreteView.getType() + " Properties", true);
      Dimension dimension = new Dimension(1280, 640);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));
      dialog.setContentPane(new JPanel());

      SwingUtilities.invokeLater(new Runnable()
      {
        @Override
        public void run()
        {
          dialog.setVisible(true);
        }
      });
    }
  }
}

