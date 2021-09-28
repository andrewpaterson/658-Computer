package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_MVN
    extends OpCode
{
  public OpCode_MVN(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address addressOfOpCodeData = cpu.getAddressOfOpCodeData(getAddressingMode());
    int destinationBank = cpu.get8BitData();
    addressOfOpCodeData.incrementOffsetBy( 1);
    int sourceBank = cpu.get8BitData();

    Address sourceAddress = new Address(sourceBank, cpu.getX());
    Address destinationAddress = new Address (destinationBank, cpu.getY());

    while (cpu.getA() != 0xFFFF)
    {
      int toTransfer = cpu.get8BitData();
      cpu.storeByte(destinationAddress, toTransfer);

      sourceAddress.incrementOffsetBy( 1);
      destinationAddress.incrementOffsetBy( 1);
      cpu.accumulator--;
      cpu.accumulator = toShort(cpu.accumulator);

      cpu.addToCycles(7);
    }
    cpu.setDataBank(destinationBank);
    cpu.addToProgramAddress(3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

