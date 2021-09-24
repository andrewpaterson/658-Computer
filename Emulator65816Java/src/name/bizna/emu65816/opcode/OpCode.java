package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public abstract class OpCode
{
  private byte mCode;
  private String mName;
  private AddressingMode mAddressingMode;

  public OpCode(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    this.mCode = mCode;
    this.mName = mName;
    this.mAddressingMode = mAddressingMode;
  }

  public byte getCode()
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

  public abstract void execute(Cpu65816 cpu);
}

