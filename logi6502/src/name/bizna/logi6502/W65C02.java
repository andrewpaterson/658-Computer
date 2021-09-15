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
      case BRK_immediate:
        switch (cycle)
        {
          case 1:
          {
            wantPush((byte) (programCounter >> 8));
            break;
          }
          case 2:
          {
            wantPush((byte) programCounter);
            break;
          }
          case 3:
          {
            wantPush((byte) (processorStatus | P_B_BIT));
            break;
          }
          case 4:
          {
            processorStatus &= ~P_D_BIT;
            processorStatus |= P_I_BIT;
            wantVPB(true);
            wantRead(vectorToPull);
            break;
          }
          case 5:
          {
            programCounter = (short) (data & 0xFF);
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
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case PHP_implied:
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
            cycle = -1;
            return;
        }
        break;
      case ORA_immediate:
        if (cycle == 1)
        {
          accumulatorOrData();
        }
        cycle = -1;
        return;
      case ASL_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          short result = (short) ((data & 0xFF) << 1);
          data = (byte) result;
          updateCarryStatus(result);
          accumulator = data;
        }
        cycle = -1;
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_0:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & 1) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_N_BIT_0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_N_BIT) == 0;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            cycle = -1;
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
            updateZeroStatus();
            data &= ~accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, true);
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case CLC_implied:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_C_BIT;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case INC_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          ++data;
          updateZeroAndNegativeStatus(data);
          accumulator = data;
        }
        cycle = -1;
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
            updateZeroStatus();
            data &= ~accumulator;
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_1:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 1)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case JSR:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantPush((byte) (programCounter >> 8));
            break;
          }
          case 2:
          {
            wantPush((byte) programCounter);
            break;
          }
          case 3:
          {
            wantRead(programCounter++);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            programCounter = address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            short result = (short) (((data & 0xFF) << 1) | ((isCarrySet()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case AND_immediate:
        if (cycle == 1)
        {
          accumulator &= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
        return;
      case ROL_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          short result = (short) (((data & 0xFF) << 1) | ((isCarrySet()) ? 1 : 0));
          data = (byte) result;
          updateCarryStatus(result);
          accumulator = data;
        }
        cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            short result = (short) (((data & 0xFF) << 1) | ((isCarrySet()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_2:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 2)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_N_BIT_N_BIT:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_N_BIT) == P_N_BIT;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) << 1) | ((isCarrySet()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case SEC_implied:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_C_BIT;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) << 1) | ((isCarrySet()) ? 1 : 0));
            data = (byte) result;
            updateCarryStatus(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_3:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 3)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            programCounter = (short) (data & 0xFF);
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            updateCarryStatus();
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case EOR_immediate:
        if (cycle == 1)
        {
          accumulator ^= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
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
        cycle = -1;
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
            address |= (short) (data << 8);
            programCounter = address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            updateCarryStatus();
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_4:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 4)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_V_BIT_0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_V_BIT) == 0;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            parent.setMLB(instanceState, true);
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
            updateCarryStatus();
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case CLI_implied:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_I_BIT;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            updateCarryStatus();
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_5:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 5)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            programCounter = (short) (data & 0xFF);
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
            cycle = -1;
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
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 6:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case STZ_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            cycle = -1;
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
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            short result = (short) (((data & 0xFF) >> 1) | (isCarrySet() ? 0x80 : 0));
            updateCarryStatus();
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case ADC_immediate:
        switch (cycle)
        {
          case 1:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case ROR_implied_a:
        if (cycle == 1)
        {
          programCounter--;
          data = accumulator;
          short result = (short) (((data & 0xFF) >> 1) | (isCarrySet() ? 0x80 : 0));
          updateCarryStatus();
          data = (byte) result;
          updateZeroAndNegativeStatus(result);
          accumulator = data;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            programCounter = address;
          }
          default:
            cycle = -1;
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
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            short result = (short) (((data & 0xFF) >> 1) | (isCarrySet() ? 0x80 : 0));
            updateCarryStatus();
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_6:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 6)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_V_BIT_V_BIT:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_V_BIT) == P_V_BIT;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 6:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) >> 1) | (isCarrySet() ? 0x80 : 0));
            updateCarryStatus();
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case SEI_implied:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_I_BIT;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            address |= (short) (data << 8);
            programCounter = address;
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & (data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) >> 1) | (isCarrySet() ? 0x80 : 0));
            updateCarryStatus();
            data = (byte) result;
            updateZeroAndNegativeStatus(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBR_relative_bit_branch_7:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 7)) == 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_0_0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = true;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case STY_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            data = yIndex;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case STA_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case STX_zero_page:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            data = xIndex;
            wantWrite(address, data);
            break;
          }
          case 2:
          {
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
        cycle = -1;
        return;
      case BIT_immediate_immediate:
        if (cycle == 1)
        {
          updateZeroStatus();
        }
        cycle = -1;
        return;
      case TXA_implied:
        if (cycle == 1)
        {
          programCounter--;
          accumulator = xIndex;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case BBS_relative_bit_branch_0:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_C_BIT_0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_C_BIT) == 0;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
            cycle = -1;
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
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case TXS_implied:
        if (cycle == 1)
        {
          programCounter--;
          stack = xIndex;
        }
        cycle = -1;
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0x9e:
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
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0x9f:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 1)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa0:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 0xa1:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa2:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 0xa4:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa5:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa6:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa7:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xa8:
        if (cycle == 1)
        {
          programCounter--;
          yIndex = accumulator;
          updateZeroAndNegativeStatus(yIndex);
        }
        cycle = -1;
        return;
      case (byte) 0xa9:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          accumulator = data;
        }
        cycle = -1;
        return;
      case (byte) 0xaa:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = accumulator;
          updateZeroAndNegativeStatus(xIndex);
        }
        cycle = -1;
        return;
      case (byte) 0xac:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xad:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xae:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xaf:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 2)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_C_BIT) == P_C_BIT;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb1:
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
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb2:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb4:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb5:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb6:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb7:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xb8:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_V_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0xb9:
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
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xba:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = stack;
          updateZeroAndNegativeStatus(xIndex);
        }
        cycle = -1;
        return;
      case (byte) 0xbc:
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
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xbd:
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
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xbe:
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
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xbf:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 3)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc0:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
        return;
      case (byte) 0xc1:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc2:
      case (byte) 0xe2:

        cycle = -1;
        return;
      case (byte) 0xc4:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc5:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc6:
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc7:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xc8:
        if (cycle == 1)
        {
          programCounter--;
          data = yIndex;
          ++data;
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 0xc9:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
        return;
      case (byte) 0xca:
        if (cycle == 1)
        {
          programCounter--;
          data = xIndex;
          data--;
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 0xcb:
        switch (cycle)
        {
          case 1:
          {
            programCounter--;
            parent.setRDY(instanceState, false);
            break;
          }
          case 2:
          {
            if (!parent.getIRQB(instanceState) && !(parent.getNMIB(instanceState) && !previousNMI))
            {
              --cycle;
            }
            break;
          }
          case 3:
          {
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xcc:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xcd:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xce:
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xcf:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 4)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd0:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_Z_BIT) == 0;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd1:
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
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd2:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd5:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd6:
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
            parent.setMLB(instanceState, true);
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd7:
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
            cycle = -1;
            return;
        }
        break;
      case (byte) 0xd8:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_D_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0xd9:
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
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
            return;
        }
        break;
      case (byte) PHX_implied:
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
            cycle = -1;
            return;
        }
        break;
      case STP_implied:
        if (cycle == 1)
        {
          programCounter--;
          stopped = true;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
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
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBS_relative_bit_branch_5:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 5)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case CPX_immediate:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
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
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 6:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            ++data;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case INC_implied_x:
        if (cycle == 1)
        {
          programCounter--;
          data = xIndex;
          ++data;
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        cycle = -1;
        return;
      case SBC_immediate:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            ++data;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBS_relative_bit_branch_6:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 6)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case Branch_relative_Z_BIT_Z_BIT:
        switch (cycle)
        {
          case 1:
          {
            address = (short) (programCounter + data);
            takingBranch = (processorStatus & P_Z_BIT) == P_Z_BIT;
            boolean doFixup = (programCounter >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 6:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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

          cycle = -1;
          return;
        }
        else
        {
          cycle = -1;
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
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 4:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            parent.setMLB(instanceState, true);
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
            ++data;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
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
            cycle = -1;
            return;
        }
        break;
      case SED_implied:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_D_BIT;
        }
        cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = offsetAddressByY();
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            cycle = -1;
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
          address |= (short) (data << 8);
          short offsetAddress = (short) (address + (xIndex & 0xFF));
          boolean doFixup = (address >> 8) != (offsetAddress >> 8);
          address = offsetAddress;
          if (doFixup)
          {
            wantRead((short) (address - 0x100));
          }
          if (!doFixup)
          {
            cycle++;
            doInstruction();
          }
        }
        else
        {

          cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if (isCarrySet())
            {
              address++;
            }
            if ((processorStatus & P_D_BIT) != 0)
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
            if ((processorStatus & P_D_BIT) == 0)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 5:
          {
            if (((accumulator ^ address) & ~(data ^ address) & 0x80) != 0)
            {
              processorStatus |= P_V_BIT;
            }
            else
            {
              processorStatus &= ~P_V_BIT;
            }
            updateCarryStatus(address);
            accumulator = (byte) address;
          }
          default:
            cycle = -1;
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
            address |= (short) (data << 8);
            short offsetAddress = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (offsetAddress >> 8);
            address = offsetAddress;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              cycle++;
              doInstruction();
            }
            break;
          }
          case 3:
          {
            parent.setMLB(instanceState, true);
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
            ++data;
            updateZeroAndNegativeStatus(data);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            cycle = -1;
            return;
        }
        break;
      case BBS_relative_bit_branch_7:
        switch (cycle)
        {
          case 1:
          {
            address = data;
            wantRead(programCounter++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (programCounter + off);
            wantRead((short) ((programCounter & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 7)) != 0;
            if (takingBranch)
            {
              programCounter = address;
            }
          }
          default:
            cycle = -1;
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
          address = (short) (data & 0xFF);
        }
        cycle = -1;
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
        cycle = -1;
    }
  }

  private boolean isCarrySet()
  {
    return (processorStatus & P_C_BIT) != 0;
  }

  private void updateCarryStatus()
  {
    if ((data & 1) != 0)
    {
      processorStatus |= P_C_BIT;
    }
    else
    {
      processorStatus &= ~P_C_BIT;
    }
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
    address = (short) (data & 0xFF);
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
    address = (short) (data & 0xFF);
    wantRead(address);
  }

  private void readNextAddressAddressLowFromData()
  {
    wantRead((short) (address + 1));
    address = (short) (data & 0xFF);
  }

  private void offsetAddressByXReadAddress()
  {
    address = offsetAddressByX();
    wantRead(address);
  }

  private void readAddressHighSetMLB()
  {
    address |= (short) (data << 8);
    parent.setMLB(instanceState, true);
    wantRead(address);
  }

  private void readAddressLowSetMLB()
  {
    address = (short) (data & 0xFF);
    parent.setMLB(instanceState, true);
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

