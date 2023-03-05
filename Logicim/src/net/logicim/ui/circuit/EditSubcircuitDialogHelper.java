package net.logicim.ui.circuit;

import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;

public class EditSubcircuitDialogHelper
{
  public EditSubcircuitDialogHelper()
  {
  }

  public void showPropertyEditorDialog(NewSubcircuitDialog newSubcircuitDialog)
  {
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    NewSubcircuitDialog dialog = newSubcircuitDialog;
    dialog.build();
    mousePosition.x -= 50;
    mousePosition.y -= 50;
    dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dialog.getDimension()));

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

