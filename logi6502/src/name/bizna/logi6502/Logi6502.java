package name.bizna.logi6502;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public abstract class Logi6502
    extends InstanceFactory
{
  public Logi6502(String name)
  {
    super(name);
  }

  void paintPorts(InstancePainter painter, PortInfo[] portInfos, int pinsPerSide)
  {
    int n = 0;
    for (int i = 0; i < portInfos.length; ++i)
    {
      if (portInfos[i] != null)
      {
        Direction dir = i < pinsPerSide ? Direction.EAST : Direction.WEST;
        painter.drawPort(n, portInfos[i].name, dir);
        ++n;
      }
    }
  }

  protected abstract boolean isReset(InstanceState i);

  protected abstract boolean getPHI2(InstanceState i);

  public boolean isReady(InstanceState i)
  {
    return true;
  }

  public boolean isOverflow(InstanceState i)
  {
    return false;
  }

  public abstract boolean isInterruptRequest(InstanceState i);

  public abstract boolean isNonMaskableInterrupt(InstanceState i);

  public abstract void doRead(InstanceState i, short a);

  public abstract void doWrite(InstanceState i, short a, byte data);

  public abstract byte getDataFromPort(InstanceState i);

  public void setReady(InstanceState i, boolean x)
  {
  }

  public void setVPB(InstanceState i, boolean x)
  {
  }

  public void setSync(InstanceState i, boolean x)
  {
    System.out.println("SyncPSYCH");
  }

  public void setMLB(InstanceState i, boolean x)
  {
  }

  @Override
  public void propagate(InstanceState state)
  {
    CoreState core = CoreState.get(state, this);
    core.tick(state, isReset(state), getPHI2(state));
  }

  @Override
  public void paintInstance(InstancePainter painter)
  {
    Graphics g = painter.getGraphics();
    if (g instanceof Graphics2D)
    {
      Font oldFont = g.getFont();
      g.setFont(oldFont.deriveFont(Font.BOLD));
      Bounds bds = painter.getBounds();
      Graphics2D g2 = (Graphics2D) g;
      AffineTransform oldTransform = g2.getTransform();
      AffineTransform newTransform = (AffineTransform) oldTransform.clone();
      newTransform.translate(bds.getX() + bds.getWidth() / 2.0, bds.getY() + bds.getHeight() / 2.0);
      if (bds.getWidth() < bds.getHeight())
      {
        newTransform.quadrantRotate(-1, 0, 0);
      }
      g2.setTransform(newTransform);
      GraphicsUtil.drawCenteredText(g, "W65C02S", 0, 0);
      g2.setTransform(oldTransform);
      g.setFont(oldFont);
    }
  }

  protected void shred(InstanceState state)
  {
    CoreState core = CoreState.get(state, this);
    core.shred();
  }

  protected void addStandardPins(PortInfo[] portInfos, int LEFT_X, int RIGHT_X, int PIN_START_Y, int PIN_STOP_Y, int PIXELS_PER_PIN, int PINS_PER_SIDE)
  {
    ArrayList<Port> ports = new ArrayList<>(portInfos.length);
    for (int n = 0; n < portInfos.length; ++n)
    {
      PortInfo info = portInfos[n];
      if (info == null)
      {
        continue;
      }
      boolean isRightSide = n >= PINS_PER_SIDE;
      int pinPerSide = isRightSide ? n - PINS_PER_SIDE : n;
      Port port;
      if (isRightSide)
      {
        port = new Port(RIGHT_X, PIN_STOP_Y - pinPerSide * PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      else
      {
        port = new Port(LEFT_X, PIN_START_Y + pinPerSide * PIXELS_PER_PIN, info.type, info.bitWidth, info.exclusive);
      }
      port.setToolTip(new StringGetter()
      {
        @Override
        public String toString()
        {
          return info.name;
        }
      });
      ports.add(port);
    }
    setPorts(ports);
  }
}

