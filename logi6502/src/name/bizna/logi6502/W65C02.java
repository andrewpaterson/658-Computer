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
      case Brk:
        switch (stage)
        {
          case 1:
          {
            wantPush((byte) (pc >> 8));
            break;
          }
          case 2:
          {
            wantPush((byte) pc);
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
            pc = (short) (data & 0xFF);
            wantRead((short) (vectorToPull + 1));
            vectorToPull = IRQ_VECTOR;
            break;
          }
          case 6:
          {
            pc |= data << 8;
            wantVPB(false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case Ora_ZpX_:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case TsbZp:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
            data |= accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case OraZp:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case AslZp:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            short result = (short) ((data & 0xFF) << 1);
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case Slo:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case Php:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPush(processorStatus);
            break;
          }
          case 2:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case OraImm:
        if (stage == 1)
        {
          accumulator |= data;
          simplePUpdateNZ(accumulator);
        }
        stage = -1;
        return;
      case Asl:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          short result = (short) ((data & 0xFF) << 1);
          data = (byte) result;
          simplePUpdateNZC(result);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 12:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
            data |= accumulator;
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 13:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 14:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            short result = (short) ((data & 0xFF) << 1);
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 15:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & 1) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 16:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_N_BIT) == 0;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 17:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 18:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 20:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
            data &= ~accumulator;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 21:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 22:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            short result = (short) ((data & 0xFF) << 1);
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 23:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 1);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 24:
        if (stage == 1)
        {
          --pc;
          processorStatus &= ~P_C_BIT;
        }
        stage = -1;
        return;
      case (byte) 25:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 26:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          ++data;
          simplePUpdateNZ(data);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 28:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
            data &= ~accumulator;
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 29:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            accumulator |= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 30:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            short result = (short) ((data & 0xFF) << 1);
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 31:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 1)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 32:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantPush((byte) (pc >> 8));
            break;
          }
          case 2:
          {
            wantPush((byte) pc);
            break;
          }
          case 3:
          {
            wantRead(pc++);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            pc = address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 33:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulator &= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 36:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
          }
          default:
            stage = -1;
            return;
        }
      case (byte) 37:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            accumulator &= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 38:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) << 1) | (((processorStatus & P_C_BIT) != 0) ? 1 : 0));
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 39:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 2);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 40:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPop();
            break;
          }
          case 2:
          {
            processorStatus = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 41:
        if (stage == 1)
        {
          accumulator &= data;
          simplePUpdateNZ(accumulator);
        }
        stage = -1;
        return;
      case (byte) 42:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          short result = (short) (((data & 0xFF) << 1) | (((processorStatus & P_C_BIT) != 0) ? 1 : 0));
          data = (byte) result;
          simplePUpdateNZC(result);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 44:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 45:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator &= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 46:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            short result = (short) (((data & 0xFF) << 1) | (((processorStatus & P_C_BIT) != 0) ? 1 : 0));
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 47:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 2)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 48:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_N_BIT) == P_N_BIT;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 49:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 50:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator &= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 52:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 53:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator &= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 54:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            short result = (short) (((data & 0xFF) << 1) | (((processorStatus & P_C_BIT) != 0) ? 1 : 0));
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 55:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 3);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 56:
        if (stage == 1)
        {
          --pc;
          processorStatus |= P_C_BIT;
        }
        stage = -1;
        return;
      case (byte) 57:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 58:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          --data;
          simplePUpdateNZ(data);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 60:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((data & accumulator) != 0)
            {
              processorStatus &= ~P_Z_BIT;
            }
            else
            {
              processorStatus |= P_Z_BIT;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 61:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 62:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            short result = (short) (((data & 0xFF) << 1) | (((processorStatus & P_C_BIT) != 0) ? 1 : 0));
            data = (byte) result;
            simplePUpdateNZC(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 63:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 3)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 64:
        switch (stage)
        {
          case 1:
          {
            --pc;
            address = pc;
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
            pc = (short) (data & 0xFF);
            wantPop();
            break;
          }
          case 5:
          {
            pc |= (short) (data << 8);
            wantRead(pc);
            break;
          }
          case 6:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 65:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            accumulator ^= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 69:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            accumulator ^= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 70:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 71:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 4);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 72:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPush(accumulator);
            break;
          }
          case 2:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 73:
        if (stage == 1)
        {
          accumulator ^= data;
          simplePUpdateNZ(accumulator);
        }
        stage = -1;
        return;
      case (byte) 74:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          if ((data & 1) != 0)
          {
            processorStatus |= P_C_BIT;
          }
          else
          {
            processorStatus &= ~P_C_BIT;
          }
          short result = (short) ((data & 0xFF) >> 1);
          data = (byte) result;
          simplePUpdateNZ(result);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 76:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            pc = address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 77:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator ^= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 78:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 79:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 4)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 80:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_V_BIT) == 0;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 81:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 82:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            accumulator ^= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 84:
      case (byte) 212:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 85:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            accumulator ^= data;
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 86:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 87:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 5);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 88:
        if (stage == 1)
        {
          --pc;
          processorStatus &= ~P_I_BIT;
        }
        stage = -1;
        return;
      case (byte) 89:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 90:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPush(yIndex);
            break;
          }
          case 2:
          {
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 92:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 93:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(accumulator);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 94:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            short result = (short) ((data & 0xFF) >> 1);
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 95:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 5)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 96:
        switch (stage)
        {
          case 1:
          {
            --pc;
            address = pc;
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
            pc = (short) (data & 0xFF);
            wantPop();
            break;
          }
          case 5:
          {
            pc |= (short) data << 8;
            break;
          }
          case 6:
          {
            ++pc;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 97:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 100:
        switch (stage)
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
            stage = -1;
            return;
        }
        break;
      case (byte) 101:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 102:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            short result = (short) (((data & 0xFF) >> 1) | (((processorStatus & P_C_BIT) != 0) ? 0x80 : 0));
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 103:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 6);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 104:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPop();
            break;
          }
          case 2:
          {
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 105:
        switch (stage)
        {
          case 1:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 106:
        if (stage == 1)
        {
          --pc;
          data = accumulator;
          short result = (short) (((data & 0xFF) >> 1) | (((processorStatus & P_C_BIT) != 0) ? 0x80 : 0));
          if ((data & 1) != 0)
          {
            processorStatus |= P_C_BIT;
          }
          else
          {
            processorStatus &= ~P_C_BIT;
          }
          data = (byte) result;
          simplePUpdateNZ(result);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 108:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            pc = address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 109:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 110:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            short result = (short) (((data & 0xFF) >> 1) | (((processorStatus & P_C_BIT) != 0) ? 0x80 : 0));
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 111:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 6)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 112:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_V_BIT) == P_V_BIT;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 113:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 114:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 116:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            data = 0x00;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 117:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) ((data & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 118:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            short result = (short) (((data & 0xFF) >> 1) | (((processorStatus & P_C_BIT) != 0) ? 0x80 : 0));
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 119:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data &= ~(1 << 7);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 120:
        if (stage == 1)
        {
          --pc;
          processorStatus |= P_I_BIT;
        }
        stage = -1;
        return;
      case (byte) 121:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 122:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPop();
            break;
          }
          case 2:
          {
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 124:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 5:
          {
            address |= (short) (data << 8);
            pc = address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 125:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 126:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            short result = (short) (((data & 0xFF) >> 1) | (((processorStatus & P_C_BIT) != 0) ? 0x80 : 0));
            if ((data & 1) != 0)
            {
              processorStatus |= P_C_BIT;
            }
            else
            {
              processorStatus &= ~P_C_BIT;
            }
            data = (byte) result;
            simplePUpdateNZ(result);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 127:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 7)) == 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 128:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = true;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 129:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 132:
        switch (stage)
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
            stage = -1;
            return;
        }
        break;
      case (byte) 133:
        switch (stage)
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
            stage = -1;
            return;
        }
        break;
      case (byte) 134:
        switch (stage)
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
            stage = -1;
            return;
        }
        break;
      case (byte) 135:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 136:
        if (stage == 1)
        {
          --pc;
          data = yIndex;
          --data;
          simplePUpdateNZ(data);
          yIndex = data;
        }
        stage = -1;
        return;
      case (byte) 137:
        if (stage == 1)
        {
          if ((data & accumulator) != 0)
          {
            processorStatus &= ~P_Z_BIT;
          }
          else
          {
            processorStatus |= P_Z_BIT;
          }
        }
        stage = -1;
        return;
      case (byte) 138:
        if (stage == 1)
        {
          --pc;
          accumulator = xIndex;
          simplePUpdateNZ(accumulator);
        }
        stage = -1;
        return;
      case (byte) 140:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 141:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 142:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 143:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 144:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_C_BIT) == 0;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 145:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            stage = -1;
            return;
        }
        break;
      case (byte) 146:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 148:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            data = yIndex;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 149:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            data = accumulator;
            wantWrite(address, data);
            break;
          }
          case 3:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 150:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 151:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 1;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 152:
        if (stage == 1)
        {
          --pc;
          accumulator = yIndex;
          simplePUpdateNZ(accumulator);
        }
        stage = -1;
        return;
      case (byte) 153:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            stage = -1;
            return;
        }
        break;
      case (byte) 154:
        if (stage == 1)
        {
          --pc;
          stack = xIndex;
        }
        stage = -1;
        return;
      case (byte) 156:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
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
            stage = -1;
            return;
        }
        break;
      case (byte) 157:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            stage = -1;
            return;
        }
        break;
      case (byte) 158:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            stage = -1;
            return;
        }
        break;
      case (byte) 159:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 1)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 160:
        if (stage == 1)
        {
          simplePUpdateNZ(data);
          yIndex = data;
        }
        stage = -1;
        return;
      case (byte) 161:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 162:
        if (stage == 1)
        {
          simplePUpdateNZ(data);
          xIndex = data;
        }
        stage = -1;
        return;
      case (byte) 164:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            simplePUpdateNZ(data);
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 165:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 166:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            simplePUpdateNZ(data);
            xIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 167:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 2;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 168:
        if (stage == 1)
        {
          --pc;
          yIndex = accumulator;
          simplePUpdateNZ(yIndex);
        }
        stage = -1;
        return;
      case (byte) 169:
        if (stage == 1)
        {
          simplePUpdateNZ(data);
          accumulator = data;
        }
        stage = -1;
        return;
      case (byte) 170:
        if (stage == 1)
        {
          --pc;
          xIndex = accumulator;
          simplePUpdateNZ(xIndex);
        }
        stage = -1;
        return;
      case (byte) 172:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            simplePUpdateNZ(data);
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 173:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 174:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            simplePUpdateNZ(data);
            xIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 175:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 2)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 176:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_C_BIT) == P_C_BIT;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 177:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 178:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 180:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            simplePUpdateNZ(data);
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 181:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 182:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
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
            simplePUpdateNZ(data);
            xIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 183:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 3;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 184:
        if (stage == 1)
        {
          --pc;
          processorStatus &= ~P_V_BIT;
        }
        stage = -1;
        return;
      case (byte) 185:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 186:
        if (stage == 1)
        {
          --pc;
          xIndex = stack;
          simplePUpdateNZ(xIndex);
        }
        stage = -1;
        return;
      case (byte) 188:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            yIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 189:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            accumulator = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 190:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            xIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 191:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 3)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 192:
        if (stage == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
          simplePUpdateNZC(address);
        }
        stage = -1;
        return;
      case (byte) 193:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 194:
      case (byte) 226:

        stage = -1;
        return;
      case (byte) 196:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 197:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 198:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            --data;
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 199:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 4;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 200:
        if (stage == 1)
        {
          --pc;
          data = yIndex;
          ++data;
          simplePUpdateNZ(data);
          yIndex = data;
        }
        stage = -1;
        return;
      case (byte) 201:
        if (stage == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
          simplePUpdateNZC(address);
        }
        stage = -1;
        return;
      case (byte) 202:
        if (stage == 1)
        {
          --pc;
          data = xIndex;
          --data;
          simplePUpdateNZ(data);
          xIndex = data;
        }
        stage = -1;
        return;
      case (byte) 203:
        switch (stage)
        {
          case 1:
          {
            --pc;
            parent.setRDY(instanceState, false);
            break;
          }
          case 2:
          {
            if (!parent.getIRQB(instanceState) && !(parent.getNMIB(instanceState) && !previousNMI))
            {
              --stage;
            }
            break;
          }
          case 3:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 204:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 205:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 206:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            --data;
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 207:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 4)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 208:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_Z_BIT) == 0;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 209:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 210:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 213:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 214:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            --data;
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 215:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 5;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 216:
        if (stage == 1)
        {
          --pc;
          processorStatus &= ~P_D_BIT;
        }
        stage = -1;
        return;
      case (byte) 217:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 218:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPush(xIndex);
            break;
          }
          case 2:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 219:
        if (stage == 1)
        {
          --pc;
          stopped = true;
        }
        stage = -1;
        return;
      case (byte) 220:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 3:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 221:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 222:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            --data;
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 223:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 5)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 224:
        if (stage == 1)
        {
          address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
          simplePUpdateNZC(address);
        }
        stage = -1;
        return;
      case (byte) 0xe1:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 4:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 5:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xe4:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xe5:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xe6:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            parent.setMLB(instanceState, true);
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
            ++data;
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 4:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xe7:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 6;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xe8:
        if (stage == 1)
        {
          --pc;
          data = xIndex;
          ++data;
          simplePUpdateNZ(data);
          xIndex = data;
        }
        stage = -1;
        return;
      case (byte) 0xe9:
        switch (stage)
        {
          case 1:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xec:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
            simplePUpdateNZC(address);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xed:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xee:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
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
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xef:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 6)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf0:
        switch (stage)
        {
          case 1:
          {
            address = (short) (pc + data);
            takingBranch = (processorStatus & P_Z_BIT) == P_Z_BIT;
            boolean doFixup = (pc >> 8) != (address >> 8);
            if (doFixup)
            {
              wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            }
            if (!doFixup)
            {
              ++stage;
              doInstruction();
            }
            break;
          }
          case 2:
          {
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf1:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf2:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            wantRead((short) (address + 1));
            address = (short) (data & 0xFF);
            break;
          }
          case 3:
          {
            address |= (short) (data << 8);
            wantRead(address);
            break;
          }
          case 4:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf4:
        if (stage == 1)
        {
          address = (short) (data & 0xFF);
          wantRead(address);
        }
        else if (stage == 2)
        {
          address += xIndex & 0xFF;

          stage = -1;
          return;
        }
        else
        {
          stage = -1;
          return;
        }
        break;
      case (byte) 0xf5:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
            wantRead(address);
            break;
          }
          case 3:
          {
            address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF));
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf6:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(address);
            break;
          }
          case 2:
          {
            address += xIndex & 0xFF;
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
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 5:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xf7:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
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
            data |= 1 << 7;
            wantWrite(address, data);
            break;
          }
          case 4:
          {
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 248:
        if (stage == 1)
        {
          --pc;
          processorStatus |= P_D_BIT;
        }
        stage = -1;
        return;
      case (byte) 0xf9:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (yIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xfa:
        switch (stage)
        {
          case 1:
          {
            --pc;
            wantPop();
            break;
          }
          case 2:
          {
            xIndex = data;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xfc:
        if (stage == 1)
        {
          address = (short) (data & 0xFF);
          wantRead(pc++);
        }
        else if (stage == 2)
        {
          address |= (short) (data << 8);
          short neu = (short) (address + (xIndex & 0xFF));
          boolean doFixup = (address >> 8) != (neu >> 8);
          address = neu;
          if (doFixup)
          {
            wantRead((short) (address - 0x100));
          }
          if (!doFixup)
          {
            ++stage;
            doInstruction();
          }
        }
        else
        {

          stage = -1;
          return;
        }
        break;
      case (byte) 0xfd:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            if ((processorStatus & P_C_BIT) != 0)
            {
              ++address;
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
              ++stage;
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
            simplePUpdateNZC(address);
            accumulator = (byte) address;
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xfe:
        switch (stage)
        {
          case 1:
          {
            address = (short) (data & 0xFF);
            wantRead(pc++);
            break;
          }
          case 2:
          {
            address |= (short) (data << 8);
            short neu = (short) (address + (xIndex & 0xFF));
            boolean doFixup = (address >> 8) != (neu >> 8);
            address = neu;
            if (doFixup)
            {
              wantRead((short) (address - 0x100));
            }
            if (!doFixup)
            {
              ++stage;
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
            simplePUpdateNZ(data);
            wantWrite(address, data);
            break;
          }
          case 6:
          {
            parent.setMLB(instanceState, false);
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0xff:
        switch (stage)
        {
          case 1:
          {
            address = data;
            wantRead(pc++);
            break;
          }
          case 2:
          {
            byte off = data;
            data = (byte) address;
            address = (short) (pc + off);
            wantRead((short) ((pc & 0xFF00) | (address & 0xFF)));
            takingBranch = (data & (1 << 7)) != 0;
            if (takingBranch)
            {
              pc = address;
            }
          }
          default:
            stage = -1;
            return;
        }
        break;
      case (byte) 0x02:
      case (byte) 0x22:
      case (byte) 0x62:
      case (byte) 0x82:
      case (byte) 0x42:
      case (byte) 0x44:
        if (stage == 1)
        {
          address = (short) (data & 0xFF);
        }
        stage = -1;
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
        if (stage == 1)
        {
          --pc;
        }
        stage = -1;
    }
  }
}

