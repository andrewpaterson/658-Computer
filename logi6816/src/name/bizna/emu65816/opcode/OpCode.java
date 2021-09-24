package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public abstract class OpCode
{
  private final int mCode;
  private final String mName;
  private final AddressingMode mAddressingMode;

  public OpCode(String mName, int mCode, AddressingMode mAddressingMode)
  {
    this.mCode = mCode;
    this.mName = mName;
    this.mAddressingMode = mAddressingMode;
  }

  public int getCode()
  {
    return mCode;
  }

  public String getName()
  {
    return mName;
  }

  public AddressingMode getAddressingMode()
  {
    return mAddressingMode;
  }

  public abstract void execute(Cpu65816 cpu, int cycle, boolean clock);
}

