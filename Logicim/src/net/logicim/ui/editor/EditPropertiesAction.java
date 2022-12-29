package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.components.button.Button;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class EditPropertiesAction
    extends SimulatorEditorAction
{
  protected JFrame frame;

  public EditPropertiesAction(SimulatorEditor editor, JFrame frame)
  {
    super(editor);
    this.frame = frame;
  }

  @Override
  public void execute()
  {
    DiscreteView<?> discreteView = editor.getHoverDiscreteView();
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();

    if (discreteView != null)
    {
      JDialog dialog = new JDialog(frame, discreteView.getType() + " Properties", true);
      Dimension dimension = new Dimension(360, 320);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());

      DiscreteProperties properties = discreteView.getProperties();
      contentPane.add(new PropertiesPanel(properties), gridBagConstraints(0, 0, 1, 1, BOTH));

      JPanel bottomPanel = new JPanel();
      contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));

      buildButtons(bottomPanel, new Button("Okay"), new CancelButton("Cancel", dialog));

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

