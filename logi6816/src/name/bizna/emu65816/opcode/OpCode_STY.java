package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_STY
    extends OpCode
{
  public OpCode_STY(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeSTY(OpCode &opCode)
{
  Address dataAddress = getAddressOfOpCodeData(opCode);
  if (accumulatorIs8BitWide()) {
    mSystemBus.storeByte(dataAddress, Binary::lower8BitsOf(mY));
  } else {
    mSystemBus.storeTwoBytes(dataAddress, mY);
    addToCycles(1);
  }

  switch (opCode.getCode()) {
    case(0x8C):  // STY Absolute
    {
      addToProgramAddress(3);
      addToCycles(4);
      break;
    }
    case(0x84):  // STY Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(3);
      break;
    }
    case(0x94):  // STY Direct Page Indexed, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(4);
      break;
    }
    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
