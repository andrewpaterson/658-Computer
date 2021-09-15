package name.bizna.logi6502;

import com.cburch.logisim.instance.InstanceState;

import java.util.Random;

import static name.bizna.logi6502.W6502Opcodes.Brk;
import static name.bizna.logi6502.W6502Opcodes.Wai;

public abstract class AbstractCore
{
  public static final short IRQ_VECTOR = (short) 0xFFFE;
  public static final short RESET_VECTOR = (short) 0xFFFC;
  public static final short NMI_VECTOR = (short) 0xFFFA;
  public static final byte P_C_BIT = 0x01;
  public static final byte P_Z_BIT = 0x02;
  public static final byte P_I_BIT = 0x04;
  public static final byte P_D_BIT = 0x08;
  public static final byte P_B_BIT = 0x10;
  public static final byte P_1_BIT = 0x20;
  public static final byte P_V_BIT = 0x40;
  public static final byte P_N_BIT = (byte) 0x80;

  protected Logi6502 parent;
  protected InstanceState instanceState;
  protected byte accumulator, xIndex, yIndex, processorStatus, stack, data;
  protected short address;
  protected byte fetchedOpcode, stage;
  protected short vectorToPull = IRQ_VECTOR;
  protected short pc;
  protected boolean stopped = true;
  protected boolean previousNMI = false, previousSO = false, previousClock = true, takingBranch = false, wantingSync, wantingVectorPull;
  protected short intendedAddress;
  protected byte intendedData;
  protected boolean intendedRWB;

  public AbstractCore(Logi6502 parent)
  {
    this.parent = parent;
  }

  public void shred()
  {
    Random r = new Random();
    byte[] corr = new byte[12];
    r.nextBytes(corr);
    accumulator = corr[0];
    xIndex = corr[1];
    yIndex = corr[2];
    processorStatus = corr[3];
    stack = corr[4];
    data = corr[5];
    fetchedOpcode = corr[6];
    stage = corr[7];
    address = (short) ((corr[8] & 0xFF) | corr[9] << 8);
    intendedAddress = address;
    pc = (short) ((corr[10] & 0xFF) | corr[11] << 8);
    intendedRWB = false;
    previousNMI = true;
    previousSO = true;
    wantingSync = false;
    wantingVectorPull = false;
  }

  abstract protected void doInstruction();

  protected void simplePUpdateNZ(int result)
  {
    if ((result & 0xFF) == 0)
    {
      processorStatus |= P_Z_BIT;
    }
    else
    {
      processorStatus &= ~P_Z_BIT;
    }
    if ((result & 0x80) != 0)
    {
      processorStatus |= P_N_BIT;
    }
    else
    {
      processorStatus &= ~P_N_BIT;
    }
  }

  protected void simplePUpdateNZC(int result)
  {
    simplePUpdateNZ(result);
    if ((result & 0x100) != 0)
    {
      processorStatus |= P_C_BIT;
    }
    else
    {
      processorStatus &= ~P_C_BIT;
    }
  }

  public void reset(InstanceState cis)
  {
    stopped = false;
    fetchedOpcode = Brk;
    stage = 1;
    processorStatus |= P_I_BIT;
    processorStatus &= ~P_D_BIT;
    previousNMI = true;
    vectorToPull = RESET_VECTOR;
    wantingVectorPull = false;
    parent.setVPB(cis, false);
    wantingSync = false;
    parent.setSYNC(cis, false);
    parent.setMLB(cis, false);
    parent.setRDY(cis, true);
  }

  protected void wantRead(short address)
  {
    intendedAddress = address;
    intendedRWB = true;
  }

  protected void wantWrite(short address, byte data)
  {
    intendedAddress = address;
    intendedData = data;
    intendedRWB = false;
  }

  protected void wantPush(byte data)
  {
    if (vectorToPull == RESET_VECTOR)
    {
      wantRead((short) (0x0100 | (stack & 0xFF)));
    }
    else
    {
      wantWrite((short) (0x0100 | (stack & 0xFF)), data);
    }
    --stack;
  }

  protected void wantPop()
  {
    ++stack;
    wantRead((short) (0x0100 | (stack & 0xFF)));
  }

  protected void wantVPB(boolean want)
  {
    wantingVectorPull = want;
  }

  public void goh(InstanceState instanceState, boolean isResetting, boolean clockHigh)
  {
    if (isResetting)
    {
      reset(instanceState);
    }
    boolean isReady = parent.getRDY(instanceState);
    if (!isReady && !stopped && fetchedOpcode == Wai && stage == 2)
    {
      parent.setRDY(instanceState, true);
    }
    if (stopped || !isReady)
    {
      return;
    }
    this.instanceState = instanceState;
    if (!clockHigh && previousClock)
    {
      // falling edge
      boolean so = parent.getSOB(instanceState);
      if (so != previousSO)
      {
        if (so)
        {
          processorStatus |= P_V_BIT;
        }
        previousSO = so;
      }
      do
      {
        wantingSync = stage < 0;
        if (stage < 0)
        {
          wantRead(pc++);
          ++stage;
        }
        else if (stage == 0)
        {
          wantRead(pc++);
          boolean nmi = parent.getNMIB(instanceState);
          if (nmi && !previousNMI)
          {
            vectorToPull = NMI_VECTOR;
            pc -= 2;
            fetchedOpcode = Brk;
          }
          else if (parent.getIRQB(instanceState) && (processorStatus & P_I_BIT) == 0)
          {
            vectorToPull = IRQ_VECTOR;
            pc -= 2;
            fetchedOpcode = Brk;
          }
          else
          {
            fetchedOpcode = parent.getD(instanceState);
          }
          previousNMI = nmi;
          ++stage;
        }
        else
        {
          data = parent.getD(instanceState);
          doInstruction();
          if (stage >= 0)
          {
            ++stage;
          }
        }
      }
      while (stage < 0 && !stopped);
    }
    else if (clockHigh && !previousClock)
    {
      // rising edge, apply outputs
      if (intendedRWB)
      {
        parent.doRead(instanceState, intendedAddress);
      }
      else
      {
        parent.doWrite(instanceState, intendedAddress, intendedData);
      }
      parent.setSYNC(instanceState, wantingSync);
      parent.setVPB(instanceState, wantingVectorPull);
    }
    this.instanceState = null;
    previousClock = clockHigh;
  }
}

