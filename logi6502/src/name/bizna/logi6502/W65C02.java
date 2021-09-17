package name.bizna.logi6502;

/*
 * NOTE: This source file is automatically generated! Manually editing it is
 * pointless!
 */

import static name.bizna.logi6502.W6502Opcodes.*;

/**
 * This core strictly emulates a Western Design Center-branded W65C02 or
 * W65C02S. This includes the \"Rockwell bit extensions\".
 */
public class W65C02
    extends AbstractCore
{
  public W65C02(Logi6502 parent)
  {
    super(parent);
  }

  @Override
  protected void doInstruction()
  {
    switch (fetchedOpcode)
    {
      case BRK:
        switch (cycle)
        {
          case 1:
          {
            wantPush(programCounterHighByte());
            break;
          }
          case 2:
          {
            wantPush(programCounterLowByte());
            break;
          }
          case 3:
          {
            wantPush((byte) isBreakBit());
            break;
          }
          case 4:
          {
            setDecimalMode(false);
            setInterrupt(true);
            wantVPB(true);
            wantRead(vectorToPull);
            break;
          }
          case 5:
          {
            programCounter = dataLowByte();
            wantRead((short) (vectorToPull + 1));
            vectorToPull = IRQ_VECTOR;
            break;
          }
          case 6:
          {
            programCounter |= data << 8;
            wantVPB(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TSB_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            OrAccumulator();
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ASL_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            shiftDataLeft();
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_0:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Push_processor_status:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPush(processorStatus);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_immediate:
        if (cycle == 1)
        {
          accumulatorOrData();
        }
        doneInstruction();
        return;
      case ASL_implied_a:
        if (cycle == 1)
        {
          accumulatorShiftLeft();
        }
        doneInstruction();
        return;
      case TSB_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            OrAccumulator();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ASL_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            shiftDataLeft();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_0:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(0);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Not_Negative:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_N_BIT, 0);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TRB_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            testAndResetMemoryBitsAgainstAccumulator();
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ASL_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            shiftDataLeft();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_1:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 1);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CLC_implied:
        if (cycle == 1)
        {
          programCounter--;
          setCarry(false);
        }
        doneInstruction();
        return;
      case ORA_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_implied_a:
        if (cycle == 1)
        {
          Increment();
        }
        doneInstruction();
        return;
      case TRB_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            testAndResetMemoryBitsAgainstAccumulator();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ORA_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulatorOrData();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ASL_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            shiftDataLeft();
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_1:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(1);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case JSR:
        switch (cycle)
        {
          case 1:
          {
            address = dataLowByte();
            wantPush(programCounterHighByte());
            break;
          }
          case 2:
          {
            wantPush(programCounterLowByte());
            break;
          }
          case 3:
          {
            wantRead(programCounter++);
            break;
          }
          case 4:
          {
            jumpToSubroutine();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BIT_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            updateZeroStatus();
          }
          default:
            doneInstruction();
            return;
        }
      case AND_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROL_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_2:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 2);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PLP_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPop();
            break;
          }
          case 2:
          {
            processorStatus = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_immediate:
        if (cycle == 1)
        {
          accumulator &= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        doneInstruction();
        return;
      case ROL_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
          data = (byte) result;
          updateCarryStatus(result);
          accumulator = data;
        }
        doneInstruction();
        return;
      case BIT_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            updateZeroStatus();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROL_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_2:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(2);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Negative:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_N_BIT, P_N_BIT);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BIT_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            updateZeroStatus();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROL_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_3:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 3);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SEC_implied:
        if (cycle == 1)
        {
          programCounter--;
          setCarry(true);
        }
        doneInstruction();
        return;
      case AND_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          data--;
          updateZeroAndNegativeStatus(data);
          accumulator = data;
        }
        doneInstruction();
        return;
      case BIT_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            updateZeroStatus();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case AND_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator &= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROL_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_3:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(3);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RTI_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            address = programCounter;
            wantRead(address);
            break;
          }
          case 2:
          {
            wantPop();
            break;
          }
          case 3:
          {
            processorStatus = data;
            wantPop();
            break;
          }
          case 4:
          {
            programCounter = dataLowByte();
            wantPop();
            break;
          }
          case 5:
          {
            programCounter |= (short) (data << 8);
            wantRead(programCounter);
            break;
          }
          case 6:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LSR_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            logicalShiftRight();
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_4:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 4);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PHA_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPush(accumulator);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_immediate:
        if (cycle == 1)
        {
          accumulator ^= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        doneInstruction();
        return;
      case LSR_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          updateCarryStatus();
          short result = (short) ((data & 0xFF) >> 1);
          data = (byte) result;
          updateZeroAndNegativeStatus(result);
          accumulator = data;
        }
        doneInstruction();
        return;
      case JMP_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            jumpToSubroutine();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LSR_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            logicalShiftRight();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_4:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(4);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Not_Overflow:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_V_BIT, 0);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case (byte) 0x54:
      case (byte) 0xd4:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LSR_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            logicalShiftRight();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_5:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 5);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CLI_implied:
        if (cycle == 1)
        {
          programCounter--;
          setInterrupt(false);
        }
        doneInstruction();
        return;
      case EOR_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PHY_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPush(yIndex);
            break;
          }
          case 2:
          {
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case NOP_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case EOR_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator ^= data;
            updateZeroAndNegativeStatus(accumulator);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LSR_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            logicalShiftRight();
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_5:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(5);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RTS_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            address = programCounter;
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            wantPop();
            break;
          }
          case 4:
          {
            programCounter = dataLowByte();
            wantPop();
            break;
          }
          case 5:
          {
            programCounter |= (short) data << 8;
            break;
          }
          case 6:
          {
            ++programCounter;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 6:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STZ_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = dataLowByte();
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 3:
          {
            boolean overflow = (xorAddressTestHighBit()) != 0;
            setOverflow(overflow);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROR_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            rotateRight();
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_6:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 6);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PLA_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPop();
            break;
          }
          case 2:
          {
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_immediate:
        switch (cycle)
        {
          case 1:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 2:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROR_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          short result = (short) (((data & 0xFF) >> 1) | (isCarry() ? 0x80 : 0));
          updateCarryStatus();
          data = (byte) result;
          updateZeroAndNegativeStatus(result);
          accumulator = data;
        }
        doneInstruction();
        return;
      case JMP_absolute_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            jumpToSubroutine();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 4:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROR_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            rotateRight();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_6:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(6);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Overflow:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_V_BIT, P_V_BIT);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 6:
          {
            setOverflow(xorAddressTestHighBit() != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STZ_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 4:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROR_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            rotateRight();
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case RMB_zero_page_7:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data &= ~(1 << 7);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SEI_implied:
        if (cycle == 1)
        {
          programCounter--;
          setInterrupt(true);
        }
        doneInstruction();
        return;
      case ADC_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PLY_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPop();
            break;
          }
          case 2:
          {
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case JMP_absolute_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 5:
          {
            jumpToSubroutine();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ADC_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case ROR_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            rotateRight();
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBR_relative_bit_branch_7:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitNotSet(7);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_always:
        switch (cycle)
        {
          case 1:
          {
            takingBranch = true;
            addressFromProgramCounterAddData();
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 5:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STY_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = dataLowByte();
            data = yIndex;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = dataLowByte();
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STX_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = dataLowByte();
            data = xIndex;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_0:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_implied_y:
        if (cycle == 1)
        {
          programCounter--;
          data = yIndex;
          data--;
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        doneInstruction();
        return;
      case BIT_immediate_immediate:
        if (cycle == 1)
        {
          updateZeroStatus();
        }
        doneInstruction();
        return;
      case TXA_implied:
        if (cycle == 1)
        {
          programCounter--;
          accumulator = xIndex;
          updateZeroAndNegativeStatus(accumulator);
        }
        doneInstruction();
        return;
      case STY_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            data = yIndex;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STX_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            data = xIndex;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_0:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1)) != 0;
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Not_Carry:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_C_BIT, 0);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 5:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STY_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            data = yIndex;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STX_zero_page_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address += yIndex & 0xFF;
            data = xIndex;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_1:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TYA_implied:
        if (cycle == 1)
        {
          programCounter--;
          accumulator = yIndex;
          updateZeroAndNegativeStatus(accumulator);
        }
        doneInstruction();
        return;
      case STA_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TXS_implied:
        if (cycle == 1)
        {
          programCounter--;
          stack = xIndex;
        }
        doneInstruction();
        return;
      case STZ_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STA_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STZ_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_1:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(1);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDY_immediate:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        doneInstruction();
        return;
      case LDA_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDX_immediate:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        doneInstruction();
        return;
      case LDY_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            updateZeroAndNegativeStatus(data);
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDX_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            updateZeroAndNegativeStatus(data);
            xIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_2:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 2;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TAY_implied:
        if (cycle == 1)
        {
          programCounter--;
          yIndex = accumulator;
          updateZeroAndNegativeStatus(yIndex);
        }
        doneInstruction();
        return;
      case LDA_immediate:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          accumulator = data;
        }
        doneInstruction();
        return;
      case TAX_implied:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = accumulator;
          updateZeroAndNegativeStatus(xIndex);
        }
        doneInstruction();
        return;
      case LDY_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDX_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            xIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_2:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(2);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Carry:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_C_BIT, P_C_BIT);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDY_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDX_zero_page_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address += yIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            updateZeroAndNegativeStatus(data);
            xIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_3:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 3;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CLV_implied:
        if (cycle == 1)
        {
          programCounter--;
          setOverflow(false);
        }
        doneInstruction();
        return;
      case LDA_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case TSX_implied:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = stack;
          updateZeroAndNegativeStatus(xIndex);
        }
        doneInstruction();
        return;
      case LDY_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            updateZeroAndNegativeStatus(data);
            yIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDA_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            updateZeroAndNegativeStatus(data);
            accumulator = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case LDX_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            updateZeroAndNegativeStatus(data);
            xIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_3:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(3);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CPY_immediate:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        doneInstruction();
        return;
      case CMP_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case (byte) 0xc2:
      case (byte) 0xe2:
        doneInstruction();
        return;
      case CPY_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data--;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_4:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 4;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_implied_y:
        if (cycle == 1)
        {
          programCounter--;
          data = yIndex;
          data++;
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        doneInstruction();
        return;
      case CMP_immediate:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
          updateCarryStatus(address);
        }
        doneInstruction();
        return;
      case DEC_implied_x:
        if (cycle == 1)
        {
          programCounter--;
          data = xIndex;
          data--;
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        doneInstruction();
        return;
      case WAI_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            parent.setReady(instanceState, false);
            break;
          }
          case 2:
          {
            if (!parent.isInterruptRequest(instanceState) && !(parent.isNonMaskableInterrupt(instanceState) && !previousNMI))
            {
              --cycle;
            }
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CPY_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            data--;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_4:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(4);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Not_Zero:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_Z_BIT, 0);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            data--;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_5:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 5;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CLD_implied:
        if (cycle == 1)
        {
          programCounter--;
          setDecimalMode(false);
        }
        doneInstruction();
        return;
      case CMP_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PHX_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPush(xIndex);
            break;
          }
          case 2:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case STP_implied:
        if (cycle == 1)
        {
          programCounter--;
          stopped = true;
        }
        doneInstruction();
        return;
      case (byte) 0xdc:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CMP_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case DEC_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            data--;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_5:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(5);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CPX_immediate:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        doneInstruction();
        return;
      case SBC_zero_page_x_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 4:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 5:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 6:
          {
            setOverflow(xorAddressNotTestHighBit() != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CPX_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SBC_zero_page:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 3:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_zero_page:
        switch (cycle)
        {
          case 1:
          {
            readAddressLowSetMLB();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data++;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_6:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 6;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_implied_x:
        if (cycle == 1)
        {
          programCounter--;
          data = xIndex;
          data++;
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        doneInstruction();
        return;
      case SBC_immediate:
        switch (cycle)
        {
          case 1:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 2:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case CPX_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
            updateCarryStatus(address);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SBC_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 3:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 4:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_absolute:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            readAddressHighSetMLB();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            data++;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_6:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(6);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case Branch_relative_Zero:
        switch (cycle)
        {
          case 1:
          {
            determineTakeBranchOnProcessorStatusBit(P_Z_BIT, P_Z_BIT);
            break;
          }
          case 2:
          {
            takeBranch();
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SBC_zero_page_indirect_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 6:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SBC_zero_page_indirect:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            readNextAddressAddressLowFromData();
            break;
          }
          case 3:
          {
            addressHighFromDataReadAddress();
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case (byte) 0xf4:
        if (cycle == 1)
        {
          addressLowFromDataReadAddress();
        }
        else if (cycle == 2)
        {
          address = offsetAddressByX();

          doneInstruction();
          return;
        }
        else
        {
          doneInstruction();
          return;
        }
        break;
      case SBC_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            offsetAddressByXReadAddress();
            break;
          }
          case 3:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 4:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_zero_page_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            address = offsetAddressByX();
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            data++;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SMB_zero_page_7:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadAddress();
            break;
          }
          case 2:
          {
            wantRead(address);
            break;
          }
          case 3:
          {
            data |= 1 << 7;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case SED_implied:
        if (cycle == 1)
        {
          programCounter--;
          setDecimalMode(true);
        }
        doneInstruction();
        return;
      case SBC_absolute_y:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByY();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case PLX_implied:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            wantPop();
            break;
          }
          case 2:
          {
            xIndex = data;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case (byte) 0xfc:
        if (cycle == 1)
        {
          addressLowFromDataReadProgramCounter();
        }
        else if (cycle == 2)
        {
          addressHighFromDataOffsetAddressByX();
        }
        else
        {

          doneInstruction();
          return;
        }
        break;
      case SBC_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            addWithCarryFromAccumulator(data ^ 0xFF);
            break;
          }
          case 5:
          {
            setOverflow((xorAddressNotTestHighBit()) != 0);
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case INC_absolute_x:
        switch (cycle)
        {
          case 1:
          {
            addressLowFromDataReadProgramCounter();
            break;
          }
          case 2:
          {
            addressHighFromDataOffsetAddressByX();
            break;
          }
          case 3:
          {
            setMemoryLock(true);
            wantRead(address);
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            data++;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            setMemoryLock(false);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case BBS_relative_bit_branch_7:
        switch (cycle)
        {
          case 1:
          {
            BitBranchRelative();
            break;
          }
          case 2:
          {
            takeBranchOnDataBitSet(7);
          }
          default:
            doneInstruction();
            return;
        }
        break;
      case (byte) 0x02:
      case (byte) 0x22:
      case (byte) 0x62:
      case (byte) 0x82:
      case (byte) 0x42:
      case (byte) 0x44:
        if (cycle == 1)
        {
          address = dataLowByte();
        }
        doneInstruction();
        return;
      case NOP_implied:
      case (byte) 0x03:
      case (byte) 0x23:
      case (byte) 0x53:
      case (byte) 0x63:
      case (byte) 0x83:
      case (byte) 0xab:
      case (byte) 0xb3:
      case (byte) 0x0b:
      case (byte) 0xeb:
      case (byte) 0xf3:
      case (byte) 0xfb:
      case (byte) 0x13:
      case (byte) 0x1b:
      case (byte) 0x2b:
      case (byte) 0x33:
      case (byte) 0x3b:
      case (byte) 0x43:
      case (byte) 0x4b:
      case (byte) 0x5b:
      case (byte) 0x6b:
      case (byte) 0x73:
      case (byte) 0x7b:
      case (byte) 0x8b:
      case (byte) 0x93:
      case (byte) 0x9b:
      case (byte) 0xa3:
      case (byte) 0xbb:
      case (byte) 0xc3:
      case (byte) 0xd3:
      case (byte) 0xe3:
        if (cycle == 1)
        {
          programCounter--;
        }
        doneInstruction();
    }
  }

  private void addressHighFromDataOffsetAddressByX()
  {
    address |= (short) (data << 8);
    short offsetAddress = offsetAddressByX();
    boolean doFixup = (address >> 8) != (offsetAddress >> 8);
    address = offsetAddress;
    if (doFixup)
    {
      wantRead((short) (address - 0x100));
    }
    else
    {
      cycle++;
      doInstruction();
    }
  }

  private void jumpToSubroutine()
  {
    address |= (short) (data << 8);
    programCounter = address;
  }

  private void Increment()
  {
    programCounter--;
    data = accumulator;
    data++;
    updateZeroAndNegativeStatus(data);
    accumulator = data;
  }

  private short dataLowByte()
  {
    return (short) (data & 0xFF);
  }

  private byte programCounterLowByte()
  {
    return (byte) programCounter;
  }

  private byte programCounterHighByte()
  {
    return (byte) (programCounter >> 8);
  }

  private void testAndResetMemoryBitsAgainstAccumulator()
  {
    updateZeroStatus();
    data &= ~accumulator;
    wantWrite(address, data);
  }

  private void BitBranchRelative()
  {
    address = data;
    wantRead(programCounter++);
  }

  private void rotateRight()
  {
    short result = (short) (((data & 0xFF) >> 1) | (isCarry() ? 0x80 : 0));
    updateCarryStatus();
    data = (byte) result;
    updateZeroAndNegativeStatus(result);
    wantWrite(address, data);
  }

  private void addWithCarryFromAccumulator(int data)
  {
    address = (short) ((data & 0xFF) + (accumulator & 0xFF));
    if (isCarry())
    {
      address++;
    }

    if ((isDecimalMode()) != 0)
    {
      if ((address & 0x0F) > 0x09)
      {
        address += 0x06;
      }
      if ((address & 0xF0) > 0x90)
      {
        address += 0x60;
      }
    }

    if ((isDecimalMode()) == 0)
    {
      cycle++;
      doInstruction();
    }
  }

  private void addressHighFromDataOffsetAddressByY()
  {
    address |= (short) (data << 8);
    short offsetAddress = offsetAddressByY();
    boolean doFixup = (address >> 8) != (offsetAddress >> 8);
    address = offsetAddress;
    if (doFixup)
    {
      wantRead((short) (address - 0x100));
    }
    else
    {
      cycle++;
      doInstruction();
    }
  }

  private void doneInstruction()
  {
    cycle = -1;
  }

  private void setMemoryLock(boolean mlb)
  {
    parent.setMLB(instanceState, mlb);
  }

  private void logicalShiftRight()
  {
    updateCarryStatus();
    short result = (short) ((data & 0xFF) >> 1);
    data = (byte) result;
    updateZeroAndNegativeStatus(result);
    wantWrite(address, data);
  }

  private int xorAddressTestHighBit()
  {
    return (accumulator ^ address) & (data ^ address) & 0x80;
  }

  private int xorAddressNotTestHighBit()
  {
    return (accumulator ^ address) & ~(data ^ address) & 0x80;
  }

  private int isBreakBit()
  {
    return processorStatus | P_B_BIT;
  }

  private int isDecimalMode()
  {
    return processorStatus & P_D_BIT;
  }

  private void takeBranch()
  {
    if (takingBranch)
    {
      programCounter = address;
    }
  }

  private void accumulatorShiftLeft()
  {
    programCounter--;
    data = accumulator;
    short result = (short) ((data & 0xFF) << 1);
    data = (byte) result;
    updateCarryStatus(result);
    accumulator = data;
  }

  private void takeBranchOnDataBitSet(int bit)
  {
    byte offset = data;
    data = (byte) address;
    address = (short) (programCounter + offset);
    wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
    takingBranch = (data & (1 << bit)) != 0;
    takeBranch();
  }

  private void takeBranchOnDataBitNotSet(int bit)
  {
    byte offset = data;
    data = (byte) address;
    address = (short) (programCounter + offset);
    wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
    takingBranch = (data & (1 << bit)) == 0;
    takeBranch();
  }

  private void determineTakeBranchOnProcessorStatusBit(byte processorStatusBit, int testBit)
  {
    this.takingBranch = (processorStatus & processorStatusBit) == testBit;
    addressFromProgramCounterAddData();
  }

  private void addressFromProgramCounterAddData()
  {
    address = (short) (programCounter + data);
    boolean doFixup = (programCounter >> 8) != (address >> 8);
    if (doFixup)
    {
      wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
    }
    else
    {
      cycle++;
      doInstruction();
    }
  }

  private boolean isCarry()
  {
    return (processorStatus & P_C_BIT) != 0;
  }

  private void updateCarryStatus()
  {
    setCarry((data & 1) != 0);
  }

  private short offsetAddressByY()
  {
    return (short) (address + (yIndex & 0xFF));
  }

  private short offsetAddressByX()
  {
    return (short) (address + (xIndex & 0xFF));
  }

  private void accumulatorOrData()
  {
    accumulator |= data;
    updateZeroAndNegativeStatus(accumulator);
  }

  private void addressLowFromDataReadProgramCounter()
  {
    address = dataLowByte();
    wantRead(programCounter++);
  }

  private void OrAccumulator()
  {
    updateZeroStatus();
    data |= accumulator;
    wantWrite(address, data);
  }

  private void addressHighFromDataReadAddress()
  {
    address |= (short) (data << 8);
    wantRead(address);
  }

  private void addressLowFromDataReadAddress()
  {
    address = dataLowByte();
    wantRead(address);
  }

  private void readNextAddressAddressLowFromData()
  {
    wantRead((short) (address + 1));
    address = dataLowByte();
  }

  private void offsetAddressByXReadAddress()
  {
    address = offsetAddressByX();
    wantRead(address);
  }

  private void readAddressHighSetMLB()
  {
    address |= (short) (data << 8);
    setMemoryLock(true);
    wantRead(address);
  }

  private void readAddressLowSetMLB()
  {
    address = dataLowByte();
    setMemoryLock(true);
    wantRead(address);
  }

  private void shiftDataLeft()
  {
    short result = (short) ((data & 0xFF) << 1);
    data = (byte) result;
    updateCarryStatus(result);
    wantWrite(address, data);
  }
}

