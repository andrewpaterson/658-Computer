package name.bizna.emu65816;

public class BCD16BitResult
{
  public short value;
  public boolean carry;

  public BCD16BitResult(short value, boolean carry)
  {
    this.value = value;
    this.carry = carry;
  }
}

