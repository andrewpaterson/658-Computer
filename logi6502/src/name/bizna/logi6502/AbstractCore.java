package name.bizna.logi6502;

import com.cburch.logisim.instance.InstanceState;

import java.util.Random;

import static name.bizna.logi6502.W6502Opcodes.BRK;
import static name.bizna.logi6502.W6502Opcodes.WAI_implied;

public abstract class AbstractCore
{
  public static final short IRQ_VECTOR = (short) 0xFFFE;
  public static final short RESET_VECTOR = (short) 0xFFFC;
  public static final short NMI_VECTOR = (short) 0xFFFA;
  public static final byte P_C_BIT = (byte) 0x01;
  public static final byte P_Z_BIT = (byte) 0x02;
  public static final byte P_I_BIT = (byte) 0x04;
  public static final byte P_D_BIT = (byte) 0x08;
  public static final byte P_B_BIT = (byte) 0x10;
  public static final byte P_V_BIT = (byte) 0x40;
  public static final byte P_N_BIT = (byte) 0x80;

  protected Logi6502 parent;
  protected InstanceState instanceState;
  protected byte accumulator, xIndex, yIndex, processorStatus, stack, data;
  protected short address;
  protected byte fetchedOpcode, cycle;
  protected short vectorToPull = IRQ_VECTOR;
  protected short programCounter;
  protected boolean stopped = true;
  protected boolean previousNMI = false, previousOverflow = false, previousClock = true, takingBranch = false, wantingSync, wantingVectorPull;
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
    cycle = corr[7];
    address = (short) ((corr[8] & 0xFF) | corr[9] << 8);
    intendedAddress = address;
    programCounter = (short) ((corr[10] & 0xFF) | corr[11] << 8);
    intendedRWB = false;
    previousNMI = true;
    previousOverflow = true;
    wantingSync = false;
    wantingVectorPull = false;
  }

  abstract protected void doInstruction();

  protected void updateZeroAndNegativeStatus(int result)
  {
    setZeroStatus((result & 0xFF) == 0);
    setNegative((result & 0x80) != 0);
  }

  protected void updateCarryStatus(int result)
  {
    updateZeroAndNegativeStatus(result);
    setCarry((result & 0x100) != 0);
  }

  protected void setNegative(boolean negative)
  {
    if (negative)
    {
      processorStatus |= P_N_BIT;
    }
    else
    {
      processorStatus &= ~P_N_BIT;
    }
  }

  protected void setZeroStatus(boolean zeroStatus)
  {
    if (zeroStatus)
    {
      processorStatus |= P_Z_BIT;
    }
    else
    {
      processorStatus &= ~P_Z_BIT;
    }
  }

  protected void setCarry(boolean carry)
  {
    if (carry)
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
    fetchedOpcode = BRK;
    cycle = 1;
    setInterrupt(true);
    setDecimalMode(false);
    previousNMI = true;
    vectorToPull = RESET_VECTOR;
    wantingVectorPull = false;
    parent.setVPB(cis, false);
    wantingSync = false;
    parent.setSync(cis, false);
    parent.setMLB(cis, false);
    parent.setReady(cis, true);
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
      wantRead(stackAddress());
    }
    else
    {
      wantWrite(stackAddress(), data);
    }
    stack--;
  }

  protected void wantPop()
  {
    stack++;
    wantRead(stackAddress());
  }

  private short stackAddress()
  {
    return (short) (0x0100 | (stack & 0xFF));
  }

  protected void wantVPB(boolean want)
  {
    wantingVectorPull = want;
  }

  public void tick(InstanceState instanceState, boolean isResetting, boolean clockHigh)
  {
    if (isResetting)
    {
      reset(instanceState);
    }

    boolean isReady = parent.isReady(instanceState);
    if (!isReady && !stopped && fetchedOpcode == WAI_implied && cycle == 2)
    {
      parent.setReady(instanceState, true);
    }
    if (stopped || !isReady)
    {
      return;
    }
    this.instanceState = instanceState;
    if (!clockHigh && previousClock)
    {
      // falling edge
      boolean overflow = parent.isOverflow(instanceState);
      if (overflow != previousOverflow)
      {
        if (overflow)
        {
          processorStatus |= P_V_BIT;
        }
        previousOverflow = overflow;
      }
      do
      {
        wantingSync = cycle < 0;
        if (cycle < 0)
        {
          wantRead(programCounter);
          programCounter++;
          ++cycle;
        }
        else if (cycle == 0)
        {
          wantRead(programCounter);
          programCounter++;
          boolean nmi = parent.isNonMaskableInterrupt(instanceState);
          if (nmi && !previousNMI)
          {
            vectorToPull = NMI_VECTOR;
            programCounter -= 2;
            fetchedOpcode = BRK;
          }
          else if (parent.isInterruptRequest(instanceState) && isInterrupt() == 0)
          {
            vectorToPull = IRQ_VECTOR;
            programCounter -= 2;
            fetchedOpcode = BRK;
          }
          else
          {
            fetchedOpcode = parent.getDataFromPort(instanceState);
          }
          previousNMI = nmi;
          ++cycle;
        }
        else
        {
          data = parent.getDataFromPort(instanceState);
          doInstruction();
          if (cycle >= 0)
          {
            ++cycle;
          }
        }
      }
      while (cycle < 0 && !stopped);
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
      parent.setSync(instanceState, wantingSync);
      parent.setVPB(instanceState, wantingVectorPull);
    }
    this.instanceState = null;
    previousClock = clockHigh;
  }

  private int isInterrupt()
  {
    return processorStatus & P_I_BIT;
  }

  protected void updateZeroStatus()
  {
    setZeroStatus((data & accumulator) == 0);
  }

  protected void setOverflow(boolean overflow)
  {
    if (overflow)
    {
      processorStatus |= P_V_BIT;
    }
    else
    {
      processorStatus &= ~P_V_BIT;
    }
  }

  protected void setDecimalMode(boolean decimalMode)
  {
    if (decimalMode)
    {
      processorStatus |= P_D_BIT;
    }
    else
    {
      processorStatus &= ~P_D_BIT;
    }
  }

  protected void setInterrupt(boolean interrupt)
  {
    if (interrupt)
    {
      processorStatus |= P_I_BIT;
    }
    else
    {
      processorStatus &= ~P_I_BIT;
    }
  }
}

