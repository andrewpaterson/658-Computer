package net.logicim.ui.circuit;

import net.logicim.ui.Logicim;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class NewSubcircuitDialog
    extends InputDialog
{
  protected SubcircuitPropertiesPanel propertiesPanel;

  public NewSubcircuitDialog(Frame owner,
                             Logicim editor)
  {
    super(owner,
          "Create subcircuit",
          true,
          new Dimension(DEFAULT_WIDTH, SMALL_HEIGHT),
          editor);
  }

  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    JPanel editorPanel = createEditorPanel();
    propertiesPanel = (SubcircuitPropertiesPanel) editorPanel;
    contentPane.add(editorPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    JPanel bottomPanel = buildButtons(DEFAULT_WIDTH,
                                      new ActionButton("Okay", this),
                                      new CancelButton("Cancel", this));
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  @Override
  public void okay()
  {
    String name = propertiesPanel.getSubcircuitName();
    editor.addEditorEvent(new NewSubcircuitEvent(name));

    close();
  }

  protected JPanel createEditorPanel()
  {
    return new SubcircuitPropertiesPanel("");
  }
}

