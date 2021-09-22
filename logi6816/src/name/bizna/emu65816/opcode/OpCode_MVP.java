package name.bizna.emu65816.opcode;

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

  }

  void Cpu65816::executeMisc(OpCode &opCode)
{
  switch (opCode.getCode()) {
     case(0x44):     // MVP
    {
      Address addressOfOpCodeData = getAddressOfOpCodeData(opCode);
      uint8_t destinationBank = mSystemBus.readByte(addressOfOpCodeData);
      addressOfOpCodeData.incrementOffsetBy(1);
      uint8_t sourceBank = mSystemBus.readByte(addressOfOpCodeData);

      Address sourceAddress(sourceBank, mX);
      Address destinationAddress(destinationBank, mY);

      while(mA != 0xFFFF) {
        uint8_t toTransfer = mSystemBus.readByte(sourceAddress);
        mSystemBus.storeByte(destinationAddress, toTransfer);

        sourceAddress.decrementOffsetBy(1);
        destinationAddress.decrementOffsetBy(1);
        mA--;

        addToCycles(7);
      }
      mDB = destinationBank;

      addToProgramAddress(3);
      break;
    }
     default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}

}
