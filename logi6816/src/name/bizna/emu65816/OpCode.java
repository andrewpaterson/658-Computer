package name.bizna.emu65816;

public abstract class OpCode
{
  private byte mCode;
  private String mName;
  private AddressingMode mAddressingMode;

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

  public abstract boolean execute(Cpu65816 mExecutor);
}

