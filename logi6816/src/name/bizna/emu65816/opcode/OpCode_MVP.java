package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_MVP
    extends OpCode
{
  public OpCode_MVP(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int destinationBank = cpu.readByte(addressOfOpCodeData);
    addressOfOpCodeData.incrementOffsetBy(1);
    int sourceBank = cpu.readByte(addressOfOpCodeData);

    Address sourceAddress = new Address(sourceBank, cpu.getX());
    Address destinationAddress = new Address(destinationBank, cpu.getY());

    while (cpu.getA() != 0xFFFF)
    {
      int toTransfer = cpu.readByte(sourceAddress);
      cpu.storeByte(destinationAddress, toTransfer);

      sourceAddress.decrementOffsetBy(1);
      destinationAddress.decrementOffsetBy(1);
      cpu.decA();

      cpu.addToCycles(7);
    }
    cpu.setDB(destinationBank);
    cpu.addToProgramAddress(3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

