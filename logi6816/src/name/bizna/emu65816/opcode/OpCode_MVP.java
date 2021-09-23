package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_MVP
    extends OpCode
{
  public OpCode_MVP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte destinationBank = cpu.readByte(addressOfOpCodeData);
    addressOfOpCodeData.incrementOffsetBy((short) 1);
    byte sourceBank = cpu.readByte(addressOfOpCodeData);

    Address sourceAddress = new Address(sourceBank, cpu.getX());
    Address destinationAddress = new Address(destinationBank, cpu.getY());

    while (cpu.getA() != 0xFFFF)
    {
      byte toTransfer = cpu.readByte(sourceAddress);
      cpu.storeByte(destinationAddress, toTransfer);

      sourceAddress.decrementOffsetBy((short) 1);
      destinationAddress.decrementOffsetBy((short) 1);
      cpu.decA();

      cpu.addToCycles(7);
    }
    cpu.setDB(destinationBank);
    cpu.addToProgramAddress(3);
  }
}

