package net.logicim.ui.circuit;

import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;

public class EditorDialogHelper
{
  public EditorDialogHelper()
  {
  }

  public void showDialog(InputDialog inputDialog)
  {
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    inputDialog.build();
    mousePosition.x -= 50;
    mousePosition.y -= 50;
    inputDialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, inputDialog.getDimension()));

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        inputDialog.setVisible(true);
      }
    });
  }
}

