package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCodeTransfer
    extends OpCode
{
  public OpCodeTransfer(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeTransfer(OpCode &opCode)
{
  switch (opCode.getCode())
  {
    case(0xA8):  // TAY
    {
      if ( (accumulatorIs8BitWide() && indexIs8BitWide()) ||
           (accumulatorIs16BitWide() && indexIs8BitWide()) )
      {
        uint8_t lower8BitsOfA = Binary::lower8BitsOf(mA);
        Binary::setLower8BitsOf16BitsValue(&mY, lower8BitsOfA);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
      } else {
        mY = mA;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      }
      addToProgramAddressAndCycles(1,2);
      break;
    }
    case(0xAA):  // TAX
    {
      if ( (accumulatorIs8BitWide() && indexIs8BitWide()) ||
           (accumulatorIs16BitWide() && indexIs8BitWide()) ) {
        uint8_t lower8BitsOfA = Binary::lower8BitsOf(mA);
        Binary::setLower8BitsOf16BitsValue(&mX, lower8BitsOfA);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
      } else {
        mX = mA;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      }
      addToProgramAddressAndCycles(1,2);
      break;
    }
    case(0x5B):  // TCD
    {
      mD = mA;
      mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mD);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x7B):  // TDC
    {
      mA = mD;
      mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x1B):  // TCS
    {
      uint16_t currentStackPointer = mStack.getStackPointer();
      if (mCpuStatus.emulationFlag()) {
        Binary::setLower8BitsOf16BitsValue(&currentStackPointer, Binary::lower8BitsOf(mA));
      } else {
        currentStackPointer = mA;
      }
      mStack = Stack(&mSystemBus, currentStackPointer);

      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x3B):  // TSC
    {
      mA = mStack.getStackPointer();
      mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xBA):  // TSX
    {
      uint16_t stackPointer = mStack.getStackPointer();
      if (indexIs8BitWide()) {
        uint8_t stackPointerLower8Bits = Binary::lower8BitsOf(stackPointer);
        Binary::setLower8BitsOf16BitsValue(&mX, stackPointerLower8Bits);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(stackPointerLower8Bits);
      } else {
        mX = stackPointer;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mX);
      }

      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x8A):  // TXA
    {
      if (accumulatorIs8BitWide() && indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mX);
        Binary::setLower8BitsOf16BitsValue(&mA, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else if (accumulatorIs8BitWide() && indexIs16BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mX);
        Binary::setLower8BitsOf16BitsValue(&mA, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else if (accumulatorIs16BitWide() && indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mX);
        mA = value;
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else {
        mA = mX;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      }

      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x98):  // TYA
    {
      if (accumulatorIs8BitWide() && indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mY);
        Binary::setLower8BitsOf16BitsValue(&mA, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else if (accumulatorIs8BitWide() && indexIs16BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mY);
        Binary::setLower8BitsOf16BitsValue(&mA, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else if (accumulatorIs16BitWide() && indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mY);
        mA = value;
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else {
        mA = mY;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
      }

      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x9A):  // TXS
    {
      if (mCpuStatus.emulationFlag()) {
        uint16_t newStackPointer = 0x100;
        newStackPointer |= Binary::lower8BitsOf(mX);
        mStack = Stack(&mSystemBus, newStackPointer);
      } else if (!mCpuStatus.emulationFlag() && indexIs8BitWide()) {
        mStack = Stack(&mSystemBus, Binary::lower8BitsOf(mX));
      } else if (!mCpuStatus.emulationFlag() && indexIs16BitWide()) {
        mStack = Stack(&mSystemBus, mX);
      }
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0x9B):  // TXY
    {
      if (indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mX);
        Binary::setLower8BitsOf16BitsValue(&mY, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else {
        mY = mX;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mY);
      }
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case(0xBB):  // TYX
    {
      if (indexIs8BitWide()) {
        uint8_t value = Binary::lower8BitsOf(mY);
        Binary::setLower8BitsOf16BitsValue(&mX, value);
        mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
      } else {
        mX = mY;
        mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mX);
      }
      addToProgramAddressAndCycles(1, 2);
      break;
    }

    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
