package net.logicim.ui.error;

import javax.swing.*;

public class ErrorFrame
{
  public static void createWindow(JFrame frame, RuntimeException exception)
  {
    String message = "";
    if (exception.getMessage() != null)
    {
      message = ": " + exception.getMessage();
    }
    JOptionPane.showMessageDialog(frame,
                                  exception.getClass().getSimpleName() + message,
                                  "Exception Occurred",
                                  JOptionPane.ERROR_MESSAGE);
  }
}

