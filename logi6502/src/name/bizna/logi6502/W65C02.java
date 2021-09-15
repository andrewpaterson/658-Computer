package name.bizna.logi6502;

/*
 * NOTE: This source file is automatically generated! Manually editing it is
 * pointless!
 */

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
      case W6502Opcodes.BRK_immediate:
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
      case W6502Opcodes.ORA_zero_page_x_indirect:
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
      case W6502Opcodes.TSB_zero_page:
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
      case W6502Opcodes.ORA_zero_page:
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
      case W6502Opcodes.ASL_zero_page:
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
      case W6502Opcodes.RMB_zero_page_0:
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
      case W6502Opcodes.PHP_implied:
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
      case W6502Opcodes.ORA_immediate:
        if (cycle == 1)
        {
          accumulatorOrData();
        }
        cycle = -1;
        return;
      case W6502Opcodes.ASL_implied_a:
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
      case (byte) 0xc:
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
      case (byte) 0xd:
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
      case (byte) 0x0e:
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
      case (byte) 0x0f:
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
      case (byte) 0x10:
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
      case (byte) 0x11:
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
      case (byte) 0x12:
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
      case (byte) 0x14:
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
      case (byte) 0x15:
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
      case (byte) 0x16:
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
      case (byte) 0x17:
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
      case (byte) 0x18:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_C_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0x19:
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
      case (byte) 0x1a:
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
      case (byte) 0x1c:
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
      case (byte) 0x1d:
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
      case (byte) 0x1e:
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
      case (byte) 0x1f:
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
      case (byte) 0x20:
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
      case (byte) 0x21:
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
      case (byte) 0x24:
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
      case (byte) 0x25:
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
      case (byte) 0x26:
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
      case (byte) 0x27:
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
      case (byte) 0x28:
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
      case (byte) 0x29:
        if (cycle == 1)
        {
          accumulator &= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
        return;
      case (byte) 0x2a:
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
      case (byte) 0x2c:
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
      case (byte) 0x2d:
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
      case (byte) 0x2e:
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
      case (byte) 0x2f:
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
      case (byte) 0x30:
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
      case (byte) 0x31:
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
      case (byte) 0x32:
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
      case (byte) 0x34:
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
      case (byte) 0x35:
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
      case (byte) 0x36:
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
      case (byte) 0x37:
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
      case (byte) 0x38:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_C_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0x39:
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
      case (byte) 0x3a:
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
      case (byte) 0x3c:
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
      case (byte) 0x3d:
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
      case (byte) 0x3e:
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
      case (byte) 0x3f:
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
      case (byte) 0x40:
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
      case (byte) 0x41:
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
      case (byte) 0x45:
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
      case (byte) 0x46:
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
      case (byte) 0x47:
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
      case (byte) 0x48:
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
      case (byte) 0x49:
        if (cycle == 1)
        {
          accumulator ^= data;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
        return;
      case (byte) 0x4a:
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
      case (byte) 0x4c:
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
      case (byte) 0x4d:
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
      case (byte) 0x4e:
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
      case (byte) 0x4f:
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
      case (byte) 0x50:
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
      case (byte) 0x51:
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
      case (byte) 0x52:
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
      case (byte) 0x55:
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
      case (byte) 0x56:
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
      case (byte) 0x57:
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
      case (byte) 0x58:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_I_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0x59:
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
      case (byte) 0x5a:
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
      case (byte) 0x5c:
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
      case (byte) 0x5d:
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
      case (byte) 0x5e:
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
      case (byte) 0x5f:
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
      case (byte) 0x60:
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
      case (byte) 0x61:
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
      case (byte) 0x64:
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
      case (byte) 0x65:
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
      case (byte) 0x66:
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
      case (byte) 0x67:
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
      case (byte) 0x68:
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
      case (byte) 0x69:
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
      case (byte) 0x6a:
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
      case (byte) 0x6c:
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
      case (byte) 0x6d:
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
      case (byte) 0x6e:
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
      case (byte) 0x6f:
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
      case (byte) 0x70:
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
      case (byte) 0x71:
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
      case (byte) 0x72:
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
      case (byte) 0x74:
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
      case (byte) 0x75:
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
      case (byte) 0x76:
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
      case (byte) 0x77:
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
      case (byte) 0x78:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_I_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0x79:
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
      case (byte) 0x7a:
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
      case (byte) 0x7c:
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
      case (byte) 0x7d:
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
      case (byte) 0x7e:
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
      case (byte) 0x7f:
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
      case (byte) 0x80:
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
      case (byte) 0x81:
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
      case (byte) 0x84:
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
      case (byte) 0x85:
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
      case (byte) 0x86:
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
      case (byte) 0x87:
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
      case (byte) 0x88:
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
      case (byte) 0x89:
        if (cycle == 1)
        {
          updateZeroStatus();
        }
        cycle = -1;
        return;
      case (byte) 0x8a:
        if (cycle == 1)
        {
          programCounter--;
          accumulator = xIndex;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
        return;
      case (byte) 0x8c:
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
      case (byte) 0x8d:
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
      case (byte) 0x8e:
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
      case (byte) 0x8f:
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
      case (byte) 0x90:
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
      case (byte) 0x91:
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
      case (byte) 0x92:
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
      case (byte) 0x94:
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
      case (byte) 0x95:
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
      case (byte) 0x96:
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
      case (byte) 0x97:
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
      case (byte) 0x98:
        if (cycle == 1)
        {
          programCounter--;
          accumulator = yIndex;
          updateZeroAndNegativeStatus(accumulator);
        }
        cycle = -1;
        return;
      case (byte) 0x99:
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
      case (byte) 0x9a:
        if (cycle == 1)
        {
          programCounter--;
          stack = xIndex;
        }
        cycle = -1;
        return;
      case (byte) 0x9c:
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
      case (byte) 157:
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
      case (byte) 158:
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
      case (byte) 159:
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
      case (byte) 160:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          yIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 161:
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
      case (byte) 162:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          xIndex = data;
        }
        cycle = -1;
        return;
      case (byte) 164:
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
      case (byte) 165:
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
      case (byte) 166:
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
      case (byte) 167:
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
      case (byte) 168:
        if (cycle == 1)
        {
          programCounter--;
          yIndex = accumulator;
          updateZeroAndNegativeStatus(yIndex);
        }
        cycle = -1;
        return;
      case (byte) 169:
        if (cycle == 1)
        {
          updateZeroAndNegativeStatus(data);
          accumulator = data;
        }
        cycle = -1;
        return;
      case (byte) 170:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = accumulator;
          updateZeroAndNegativeStatus(xIndex);
        }
        cycle = -1;
        return;
      case (byte) 172:
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
      case (byte) 173:
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
      case (byte) 174:
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
      case (byte) 175:
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
      case (byte) 176:
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
      case (byte) 177:
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
      case (byte) 178:
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
      case (byte) 180:
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
      case (byte) 181:
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
      case (byte) 182:
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
      case (byte) 183:
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
      case (byte) 184:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_V_BIT;
        }
        cycle = -1;
        return;
      case (byte) 185:
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
      case (byte) 186:
        if (cycle == 1)
        {
          programCounter--;
          xIndex = stack;
          updateZeroAndNegativeStatus(xIndex);
        }
        cycle = -1;
        return;
      case (byte) 188:
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
      case (byte) 189:
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
      case (byte) 190:
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
      case (byte) 191:
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
      case (byte) 192:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
        return;
      case (byte) 193:
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
      case (byte) 194:
      case (byte) 226:

        cycle = -1;
        return;
      case (byte) 196:
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
      case (byte) 197:
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
      case (byte) 198:
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
      case (byte) 199:
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
      case (byte) 200:
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
      case (byte) 201:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
        return;
      case (byte) 202:
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
      case (byte) 203:
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
      case (byte) 204:
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
      case (byte) 205:
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
      case (byte) 206:
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
      case (byte) 207:
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
      case (byte) 208:
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
      case (byte) 209:
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
      case (byte) 210:
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
      case (byte) 213:
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
      case (byte) 214:
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
      case (byte) 215:
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
      case (byte) 216:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus &= ~P_D_BIT;
        }
        cycle = -1;
        return;
      case (byte) 217:
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
      case (byte) 218:
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
      case (byte) 219:
        if (cycle == 1)
        {
          programCounter--;
          stopped = true;
        }
        cycle = -1;
        return;
      case (byte) 220:
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
      case (byte) 0xdd:
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
      case (byte) 0xde:
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
      case (byte) 0xdf:
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
      case (byte) 0xe0:
        if (cycle == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
          updateCarryStatus(address);
        }
        cycle = -1;
        return;
      case (byte) 0xe1:
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
      case (byte) 0xe4:
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
      case (byte) 0xe5:
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
      case (byte) 0xe6:
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
      case (byte) 0xe7:
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
      case (byte) 0xe8:
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
      case (byte) 0xe9:
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
      case (byte) 0xec:
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
      case (byte) 0xed:
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
      case (byte) 0xee:
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
      case (byte) 0xef:
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
      case (byte) 0xf0:
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
      case (byte) 0xf1:
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
      case (byte) 0xf2:
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
      case (byte) 0xf5:
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
      case (byte) 0xf6:
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
      case (byte) 0xf7:
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
      case (byte) 248:
        if (cycle == 1)
        {
          programCounter--;
          processorStatus |= P_D_BIT;
        }
        cycle = -1;
        return;
      case (byte) 0xf9:
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
      case (byte) 0xfa:
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
      case (byte) 0xfd:
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
      case (byte) 0xfe:
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
      case (byte) 0xff:
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
      case (byte) 0x03:
      case (byte) 0x23:
      case (byte) 0x53:
      case (byte) 0x63:
      case (byte) 0x83:
      case (byte) 0xab:
      case (byte) 0xb3:
      case (byte) 0x0b:
      case (byte) 0xea:
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

