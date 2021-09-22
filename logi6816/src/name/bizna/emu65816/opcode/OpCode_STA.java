package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_STA
    extends OpCode
{
  public OpCode_STA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeSTA(OpCode &opCode)
{
  Address dataAddress = getAddressOfOpCodeData(opCode);
  if (accumulatorIs8BitWide()) {
    mSystemBus.storeByte(dataAddress, Binary::lower8BitsOf(mA));
  } else {
    mSystemBus.storeTwoBytes(dataAddress, mA);
    addToCycles(1);
  }

  switch (opCode.getCode()) {
    case(0x8D):  // STA Absolute
    {
      addToProgramAddress(3);
      addToCycles(4);
      break;
    }
    case(0x8F):  // STA Absolute Long
    {
      addToProgramAddress(4);
      addToCycles(5);
      break;
    }
    case(0x85):  // STA Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(3);
      break;
    }
    case(0x92):  // STA Direct Page Indirect
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(5);
      break;
    }
    case(0x87):  // STA Direct Page Indirect Long
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(6);
      break;
    }
    case(0x9D):  // STA Absolute Indexed, X
    {
      addToProgramAddress(3);
      addToCycles(5);
      break;
    }
    case(0x9F):  // STA Absolute Long Indexed, X
    {
      addToProgramAddress(4);
      addToCycles(5);
      break;
    }
    case(0x99):  // STA Absolute Indexed, Y
    {
      addToProgramAddress(3);
      addToCycles(5);
      break;
    }
    case(0x95):  // STA Direct Page Indexed, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(4);
      break;
    }
    case(0x81):  // STA Direct Page Indexed Indirect, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(6);
      break;
    }
    case(0x91):  // STA Direct Page Indirect Indexed, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(6);
      break;
    }
    case(0x97):  // STA Direct Page Indirect Long Indexed, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddress(2);
      addToCycles(6);
      break;
    }
    case(0x83):  // STA Stack Relative
    {
      addToProgramAddress(2);
      addToCycles(4);
      break;
    }
    case(0x93):  // STA Stack Relative Indirect Indexed, Y
    {
      addToProgramAddress(2);
      addToCycles(7);
      break;
    }
    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
