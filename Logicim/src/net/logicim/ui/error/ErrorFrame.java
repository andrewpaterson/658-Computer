package net.logicim.ui.error;

import javax.swing.*;

public class ErrorFrame
{
  public static void createWindow(JFrame frame, RuntimeException exception)
  {
    JOptionPane.showMessageDialog(frame,
                                  exception.getMessage(),
                                  "Exception Occurred",
                                  JOptionPane.ERROR_MESSAGE);
  }
}

