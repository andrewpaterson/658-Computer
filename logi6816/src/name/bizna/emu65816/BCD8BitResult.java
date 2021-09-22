package name.bizna.emu65816;

public class BCD8BitResult
{
  public byte value;
  public boolean carry;

  public BCD8BitResult(byte value, boolean carry)
  {
    this.value = value;
    this.carry = carry;
  }
}

