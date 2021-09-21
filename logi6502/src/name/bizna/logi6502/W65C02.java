package name.bizna.logi6502;

import com.cburch.logisim.instance.InstanceState;

import java.util.Random;

import static name.bizna.logi6502.W6502Opcodes.*;

public class W65C02
{
  public static final short IRQ_VECTOR = (short) 0xFFFE;
  public static final short RESET_VECTOR = (short) 0xFFFC;
  public static final short NMI_VECTOR = (short) 0xFFFA;
  public static final byte P_C_BIT = (byte) 0x01;
  public static final byte P_Z_BIT = (byte) 0x02;
  public static final byte P_I_BIT = (byte) 0x04;
  public static final byte P_D_BIT = (byte) 0x08;
  public static final byte P_B_BIT = (byte) 0x10;
  public static final byte P_V_BIT = (byte) 0x40;
  public static final byte P_N_BIT = (byte) 0x80;

  protected Logi6502 parent;
  protected InstanceState instanceState;

  protected byte cycle;

  protected byte accumulator;
  protected byte xIndex;
  protected byte yIndex;
  protected byte processorStatus;
  protected byte stack;
  protected byte data;
  protected short address;
  protected short vectorToPull;
  protected short programCounter;
  protected byte fetchedOpcode;

  protected boolean stopped;
  protected boolean previousNMI;
  protected boolean previousOverflow;
  protected boolean previousClock;
  protected boolean takingBranch;
  protected boolean wantingSync;
  protected boolean wantingVectorPull;

  protected short intendedAddress;
  protected byte intendedData;
  protected boolean intendedRWB;

  public W65C02(Logi6502 parent)
  {
    this.parent = parent;
    previousNMI = false;
    previousOverflow = false;
    previousClock = true;
    takingBranch = false;
    vectorToPull = IRQ_VECTOR;
    stopped = true;
  }

  public void shred()
  {
    Random r = new Random();
    byte[] corr = new byte[12];
    r.nextBytes(corr);
    accumulator = corr[0];
    xIndex = corr[1];
    yIndex = corr[2];
    processorStatus = corr[3];
    stack = corr[4];
    data = corr[5];
    fetchedOpcode = corr[6];
    cycle = corr[7];
    address = (short) ((corr[8] & 0xFF) | corr[9] << 8);
    intendedAddress = address;
    programCounter = (short) ((corr[10] & 0xFF) | corr[11] << 8);
    intendedRWB = false;
    previousNMI = true;
    previousOverflow = true;
    wantingSync = false;
    wantingVectorPull = false;
  }

  protected void doInstruction()
  {
    switch (fetchedOpcode)
    {
      case BRK:
        break_();
        break;
      case ORA_zero_page_x_indirect:
        orAccumulatorWithMemory_zero_page_x_indirect();
        break;
      case TSB_zero_page:
        testAndSetMemoryBitsAgainstAccumulator_zero_page();
        break;
      case ORA_zero_page:
        orAccumulatorWithMemory_zero_page();
        break;
      case ASL_zero_page:
        arithmeticShiftLeft_zero_page();
        break;
      case RMB_zero_page_0:
        clearBit(0);
        break;
      case PHP:
        push(processorStatus);
        break;
      case ORA_immediate:
        orAccumulatorWithMemory_immediate();
        break;
      case ASL_implied:
        arithmeticShiftLeftAccumulator();
        break;
      case TSB_absolute:
        testAndSetBits_absolute();
        break;
      case ORA_absolute:
        orAccumulatorWithMemory_absolute();
        break;
      case ASL_absolute:
        arithmeticShiftLeft_absolute();
        break;
      case BBR_relative_bit_branch_0:
        branchIfBitNotSet(0);
        break;
      case BPL_relative:
        branchIfPositive();
        break;
      case ORA_zero_page_indirect_y:
        orAccumulatorWithMemory_zero_page_indirect_y();
        break;
      case ORA_zero_page_indirect:
        orAccumulatorWithMemory_zero_page_indirect();
        break;
      case TRB_zero_page:
        testAndResetBits_zero_page();
        break;
      case ORA_zero_page_x:
        orAccumulatorWithMemory_zero_page_x();
        break;
      case ASL_zero_page_x:
        arithmeticShiftLeft_zero_page_x();
        break;
      case RMB_zero_page_1:
        clearBit(1);
        break;
      case CLC_implied:
        clearCarry();
        break;
      case ORA_absolute_y:
        orAccumulatorWithMemory_absolute_y();
        break;
      case INC_implied_a:
        incrementAccumulator();
        break;
      case TRB_absolute:
        testAndResetBits_absolute();
        break;
      case ORA_absolute_x:
        orAccumulatorWithMemory_absolute_x();
        break;
      case ASL_absolute_x:
        arithmeticShiftLeft_absolute_x();
        break;
      case BBR_relative_bit_branch_1:
        branchIfBitNotSet(1);
        break;
      case JSR:
        jumpToSubroutine();
        break;
      case AND_zero_page_x_indirect:
        andAccumulator_zero_page_x_indirect();
        break;
      case BIT_zero_page:
        testBits_zero_page();
        break;
      case AND_zero_page:
        andAccumulator_zero_page();
        break;
      case ROL_zero_page:
        rotateLeft_zero_page();
        break;
      case RMB_zero_page_2:
        clearBit(2);
        break;
      case PLP_implied:
        pullProcessorStatusFromStack_implied();
        break;
      case AND_immediate:
        andAccumulator_immediate();
        break;
      case ROL_implied:
        rotateLeft_implied();
        break;
      case BIT_absolute:
        testBits_absolute();
        break;
      case AND_absolute:
        andAccumulator_absolute();
        break;
      case ROL_absolute:
        rotateLeft_absolute();
        break;
      case BBR_relative_bit_branch_2:
        branchIfBitNotSet(2);
        break;
      case BMI_relative:
        branchIfNegative();
        break;
      case AND_zero_page_indirect_y:
        andAccumulator_zero_page_indirect_y();
        break;
      case AND_zero_page_indirect:
        andAccumulator_zero_page_indirect();
        break;
      case BIT_zero_page_x:
        testBits_zero_page_x();
        break;
      case AND_zero_page_x:
        andAccumulator_zero_page_x();
        break;
      case ROL_zero_page_x:
        rotateLeft_zero_page_x();
        break;
      case RMB_zero_page_3:
        clearBit(3);
      case SEC_implied:
        setCarry();
        break;
      case AND_absolute_y:
        andAccumulator_absolute_y();
        break;
      case DEC_implied_a:
        decrement_implied_a();
        break;
      case BIT_absolute_x:
        testBits_absolute_x();
        break;
      case AND_absolute_x:
        andAccumulator_absolute_x();
        break;
      case ROL_absolute_x:
        rotateLeft_absolute_x();
        break;
      case BBR_relative_bit_branch_3:
        branchIfBitNotSet(3);
        break;
      case RTI_implied:
        returnFromInterrupt();
        break;
      case EOR_zero_page_x_indirect:
        exclusiveOrAccumulator_zero_page_x_indirect();
        break;
      case EOR_zero_page:
        exclusiveOrAccumulator_zero_page();
        break;
      case LSR_zero_page:
        logicalShiftRight_zero_page();
        break;
      case RMB_zero_page_4:
        clearBit(4);
        break;
      case PHA_implied:
        push(accumulator);
        break;
      case EOR_immediate:
        exclusiveOrAccumulator_immediate();
        break;
      case LSR_implied_a:
        logicalShiftRight_implied();
        break;
      case JMP_absolute:
        jump_absolute();
        break;
      case EOR_absolute:
        exclusiveOrAccumulator_absolute();
        break;
      case LSR_absolute:
        logicalShiftRight_absolute();
        break;
      case BBR_relative_bit_branch_4:
        branchIfBitNotSet(4);
        break;
      case BVS_relative:
        branchIfNotOverflow();
        break;
      case EOR_zero_page_indirect_y:
        exclusiveOrAccumulator_zero_page_indirect_y();
        break;
      case EOR_zero_page_indirect:
        exclusiveOrAccumulator_zero_page_indirect();
        break;
      case (byte) 0x54:
      case (byte) 0xd4:
        invalid_1();
        break;
      case EOR_zero_page_x:
        exclusiveOrAccumulator_zero_page_x();
        break;
      case LSR_zero_page_x:
        logicalShiftRight_zero_page_x();
        break;
      case RMB_zero_page_5:
        clearBit(5);
        break;
      case CLI_implied:
        clearInterrupt();
        break;
      case EOR_absolute_y:
        exclusiveOrAccumulator_absolute_y();
        break;
      case PHY_implied:
        pushYOntoStack();
        break;
      case NOP_absolute:
        noOperation_absolute();
        break;
      case EOR_absolute_x:
        exclusiveOrAccumulator_absolute_x();
        break;
      case LSR_absolute_x:
        logicalShiftRight_absolute_x();
        break;
      case BBR_relative_bit_branch_5:
        branchIfBitNotSet(5);
        break;
      case RTS_implied:
        returnFromSubroutine_implied();
        break;
      case ADC_zero_page_x_indirect:
        addWithCarry_zero_page_x_indirect();
        break;
      case STZ_zero_page:
        storeZero_zero_page();
        break;
      case ADC_zero_page:
        addWithCarry_zero_page();
        break;
      case ROR_zero_page:
        rotateRight_zero_page();
        break;
      case RMB_zero_page_6:
        clearBit(6);
        break;
      case PLA_implied:
        pullAccumulatorFromStack();
        break;
      case ADC_immediate:
        addWithCarry_immediate();
        break;
      case ROR_implied:
        rotateRight_implied();
        break;
      case JMP_absolute_indirect:
        jump_absolute_indirect();
        break;
      case ADC_absolute:
        addWithCarry_absolute();
        break;
      case ROR_absolute:
        rotateRight_absolute();
        break;
      case BBR_relative_bit_branch_6:
        branchIfBitNotSet(6);
        break;
      case BVC_relative:
        branchIfOverflow();
        break;
      case ADC_zero_page_indirect_y:
        addWithCarry_zero_page_indirect_y();
        break;
      case ADC_zero_page_indirect:
        addWithCarry_zero_page_indirect();
        break;
      case STZ_zero_page_x:
        storeZero_zero_page_x();
        break;
      case ADC_zero_page_x:
        addWithCarry_zero_page_x();
        break;
      case ROR_zero_page_x:
        rotateRight_zero_page_x();
        break;
      case RMB_zero_page_7:
        clearBit(7);
        break;
      case SEI_implied:
        setInterrupt();
        break;
      case ADC_absolute_y:
        addWithCarry_absolute_y();
        break;
      case PLY_implied:
        pullYFromStack();
        break;
      case JMP_absolute_x_indirect:
        jump_absolute_x_indirect();
        break;
      case ADC_absolute_x:
        addWithCarry_absolute_x();
        break;
      case ROR_absolute_x:
        rotateRight_absolute_x();
        break;
      case BBR_relative_bit_branch_7:
        branchIfBitNotSet(7);
        break;
      case BRA_relative:
        branchAlways();
        break;
      case STA_zero_page_x_indirect:
        storeAccumulator_zero_page_x_indirect();
        break;
      case STY_zero_page:
        storeY_zero_page();
        break;
      case STA_zero_page:
        storeAccumulator_zero_page();
        break;
      case STX_zero_page:
        storeX_zero_page();
        break;
      case SMB_zero_page_0:
        setMemoryBit_zero_page(0);
        break;
      case DEC_implied_y:
        decrement_implied_y();
        break;
      case BIT_immediate_immediate:
        testBits_immediate_immediate();
        break;
      case TXA_implied:
        transferXToAccumulator();
        break;
      case STY_absolute:
        storeY_absolute();
        break;
      case STA_absolute:
        storeAccumulator_absolute();
        break;
      case STX_absolute:
        storeX_absolute();
        break;
      case BBS_relative_bit_branch_0:
        branchIfBitSet(0);
        break;
      case BCC_relative:
        branchIfNotCarry();
        break;
      case STA_zero_page_indirect_y:
        storeAccumulator_zero_page_indirect_y();
        break;
      case STA_zero_page_indirect:
        storeAccumulator_zero_page_indirect();
        break;
      case STY_zero_page_x:
        storeY_zero_page_x();
        break;
      case STA_zero_page_x:
        storeAccumulator_zero_page_x();
        break;
      case STX_zero_page_y:
        storeX_zero_page_y();
        break;
      case SMB_zero_page_1:
        setMemoryBit_zero_page(1);
        break;
      case TYA_implied:
        transferYtoAccumulator();
        break;
      case STA_absolute_y:
        storeAccumulator_absolute_y();
        break;
      case TXS_implied:
        transferXToStackPointer();
        break;
      case STZ_absolute:
        storeZero_absolute();
        break;
      case STA_absolute_x:
        storeAccumulator_absolute_x();
        break;
      case STZ_absolute_x:
        storeZero_absolute_x();
        break;
      case BBS_relative_bit_branch_1:
        branchIfBitSet(1);
        break;
      case LDY_immediate:
        loadY_immediate();
        break;
      case LDA_zero_page_x_indirect:
        loadAccumulator_zero_page_x_indirect();
        break;
      case LDX_immediate:
        loadX_immediate();
        break;
      case LDY_zero_page:
        loadY_zero_page();
        break;
      case LDA_zero_page:
        loadAccumulator_zero_page();
        break;
      case LDX_zero_page:
        loadX_zero_page();
        break;
      case SMB_zero_page_2:
        setMemoryBit_zero_page(2);
        break;
      case TAY_implied:
        transferAccumulatorToY();
        break;
      case LDA_immediate:
        loadAccumulator_immediate();
        break;
      case TAX_implied:
        transferAccumulatorToX();
        break;
      case LDY_absolute:
        loadY_absolute();
        break;
      case LDA_absolute:
        loadAccumulator_absolute();
        break;
      case LDX_absolute:
        loadX_absolute();
        break;
      case BBS_relative_bit_branch_2:
        branchIfBitSet(2);
        break;
      case BCS_relative:
        branchIfCarry();
        break;
      case LDA_zero_page_indirect_y:
        extracted_zero_page_indirect_y();
        break;
      case LDA_zero_page_indirect:
        loadAccumulator_zero_page_indirect();
        break;
      case LDY_zero_page_x:
        loadY_zero_page_x();
        break;
      case LDA_zero_page_x:
        loadAccumulator_zero_page_x();
        break;
      case LDX_zero_page_y:
        loadX_zero_page_y();
        break;
      case SMB_zero_page_3:
        setMemoryBit_zero_page(3);
        break;
      case CLV_implied:
        clearOverflow();
        break;
      case LDA_absolute_y:
        loadAccumulator_absolute_y();
        break;
      case TSX_implied:
        transferStackPointerToX();
        break;
      case LDY_absolute_x:
        loadY_absolute_x();
        break;
      case LDA_absolute_x:
        loadAccumulator_absolute_x();
        break;
      case LDX_absolute_y:
        loadX_absolute_y();
        break;
      case BBS_relative_bit_branch_3:
        branchIfBitSet(3);
        break;
      case CPY_immediate:
        compareYWithMemory_immediate();
        break;
      case CMP_zero_page_x_indirect:
        compareAccumulatorWithMemory_zero_page_x_indirect();
        break;
      case (byte) 0xc2:
      case (byte) 0xe2:
        doneInstruction();
        break;
      case CPY_zero_page:
        compareYWithMemory_zero_page();
        break;
      case CMP_zero_page:
        compareAccumulatorWithMemory_zero_page();
        break;
      case DEC_zero_page:
        decrement_zero_page();
        break;
      case SMB_zero_page_4:
        setMemoryBit_zero_page(4);
        break;
      case INC_implied_y:
        incrementY();
        break;
      case CMP_immediate:
        compareAccumulatorWithMemory_immediate();
        break;
      case DEC_implied_x:
        decrement_implied_x();
        break;
      case WAI_implied:
        waitForInterrupt();
        break;
      case CPY_absolute:
        compareYWithMemory_absolute();
        break;
      case CMP_absolute:
        compareAccumulatorWithMemory_absolute();
        break;
      case DEC_absolute:
        decrement_absolute();
        break;
      case BBS_relative_bit_branch_4:
        branchIfBitSet(4);
        break;
      case BEQ_relative:
        branchIfNotZero();
        break;
      case CMP_zero_page_indirect_y:
        compareAccumulatorWithMemory_zero_page_indirect_y();
        break;
      case CMP_zero_page_indirect:
        compareAccumulatorWithMemory_zero_page_indirect();
        break;
      case CMP_zero_page_x:
        compareAccumulatorWithMemory_zero_page_x();
        break;
      case DEC_zero_page_x:
        decrement_zero_page_x();
        break;
      case SMB_zero_page_5:
        setMemoryBit_zero_page(5);
        break;
      case CLD_implied:
        clearDecimalMode();
        break;
      case CMP_absolute_y:
        compareAccumulatorWithMemory_absolute_y();
        break;
      case PHX_implied:
        push(xIndex);
        break;
      case STP_implied:
        stop();
        break;
      case (byte) 0xdc:
        invalid5();
        break;
      case CMP_absolute_x:
        compareAccumulatorWithMemory_absolute_x();
        break;
      case DEC_absolute_x:
        decrement_absolute_x();
        break;
      case BBS_relative_bit_branch_5:
        branchIfBitSet(5);
        break;
      case CPX_immediate:
        compareXWithMemory_immediate();
        break;
      case SBC_zero_page_x_indirect:
        subtractWithCarry_zero_page_x_indirect();
        break;
      case CPX_zero_page:
        compareXWithMemory_zero_page();
        break;
      case SBC_zero_page:
        subtractWithCarry_zero_page();
        break;
      case INC_zero_page:
        incrementMemory_zero_page();
        break;
      case SMB_zero_page_6:
        setMemoryBit_zero_page(6);
        break;
      case INC_implied_x:
        incrementX();
        break;
      case SBC_immediate:
        subtractWithCarry_immediate();
        break;
      case CPX_absolute:
        compareXWithMemory_absolute();
        break;
      case SBC_absolute:
        subtractWithCarry_absolute();
        break;
      case INC_absolute:
        incrementMemory_absolute();
        break;
      case BBS_relative_bit_branch_6:
        branchIfBitSet(6);
        break;
      case BNE_relative:
        branchIfZero();
        break;
      case SBC_zero_page_indirect_y:
        subtractWithCarry_zero_page_indirect_y();
        break;
      case SBC_zero_page_indirect:
        subtractWithCarry_zero_page_indirect();
        break;
      case (byte) 0xf4:
        invalid2();
        break;
      case SBC_zero_page_x:
        subtractWithCarry_zero_page_x();
        break;
      case INC_zero_page_x:
        incrementMemory_zero_page_x();
        break;
      case SMB_zero_page_7:
        setMemoryBit_zero_page(7);
        break;
      case SED_implied:
        setDecimalMode();
        break;
      case SBC_absolute_y:
        subtractWithCarry_absolute_y();
        break;
      case PLX_implied:
        pullXFromStack();
        break;
      case (byte) 0xfc:
        invalid4();
        break;
      case SBC_absolute_x:
        subtractWithCarry_absolute_x();
        break;
      case INC_absolute_x:
        incrementMemory_absolute_x();
        break;
      case BBS_relative_bit_branch_7:
        branchIfBitSet(7);
        break;
      case (byte) 0x02:
      case (byte) 0x22:
      case (byte) 0x62:
      case (byte) 0x82:
      case (byte) 0x42:
      case (byte) 0x44:
        invalid3();
        break;
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
        noOperation();
        break;
    }
  }

  private void waitForInterrupt()
  {
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
          cycle--;
        }
        break;
      }
      case 3:
      {
      }
      default:
        doneInstruction();
    }
  }

  private void compareYWithMemory_absolute()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareYWithMemory_zero_page()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareYWithMemory_immediate()
  {
    if (cycle == 1)
    {
      address = (short) (((data ^ 0xFF) & 0xFF) + (yIndex & 0xFF) + 1);
      updateCarry(address);
    }
    doneInstruction();
  }

  private void loadAccumulator_zero_page_indirect()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void branchIfCarry()
  {
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
    }
  }

  protected String getOpcodeMnemonic()
  {
    switch (fetchedOpcode)
    {
      case BRK:
        return "BRK";
      case ORA_zero_page_x_indirect:
        return "ORA";
      case TSB_zero_page:
        return "TSB";
      case ORA_zero_page:
        return "ORA";
      case ASL_zero_page:
        return "ASL";
      case RMB_zero_page_0:
        return "RMB";
      case PHP:
        return "PHP";
      case ORA_immediate:
        return "ORA";
      case ASL_implied:
        return "ASL";
      case TSB_absolute:
        return "TSB";
      case ORA_absolute:
        return "ORA";
      case ASL_absolute:
        return "ASL";
      case BBR_relative_bit_branch_0:
        return "BBR";
      case BPL_relative:
        return "BPL";
      case ORA_zero_page_indirect_y:
        return "ORA";
      case ORA_zero_page_indirect:
        return "ORA";
      case TRB_zero_page:
        return "TRB";
      case ORA_zero_page_x:
        return "ORA";
      case ASL_zero_page_x:
        return "SL";
      case RMB_zero_page_1:
        return "RMB";
      case CLC_implied:
        return "CLC";
      case ORA_absolute_y:
        return "ORA";
      case INC_implied_a:
        return "INC";
      case TRB_absolute:
        return "TRB";
      case ORA_absolute_x:
        return "ORA";
      case ASL_absolute_x:
        return "ASL";
      case BBR_relative_bit_branch_1:
        return "BBR";
      case JSR:
        return "JSR";
      case AND_zero_page_x_indirect:
        return "AND";
      case BIT_zero_page:
        return "BIT";
      case AND_zero_page:
        return "AND";
      case ROL_zero_page:
        return "ROL";
      case RMB_zero_page_2:
        return "RMB";
      case PLP_implied:
        return "PLP";
      case AND_immediate:
        return "AND";
      case ROL_implied:
        return "ROL";
      case BIT_absolute:
        return "BIT";
      case AND_absolute:
        return "AND";
      case ROL_absolute:
        return "ROL";
      case BBR_relative_bit_branch_2:
        return "BBR";
      case BMI_relative:
        return "BMI";
      case AND_zero_page_indirect_y:
        return "AND";
      case AND_zero_page_indirect:
        return "AND";
      case BIT_zero_page_x:
        return "BIT";
      case AND_zero_page_x:
        return "AND";
      case ROL_zero_page_x:
        return "ROL";
      case RMB_zero_page_3:
        return "RMB";
      case SEC_implied:
        return "SEC";
      case AND_absolute_y:
        return "AND";
      case DEC_implied_a:
        return "DEC";
      case BIT_absolute_x:
        return "BIT";
      case AND_absolute_x:
        return "AND";
      case ROL_absolute_x:
        return "ROL";
      case BBR_relative_bit_branch_3:
        return "BBR";
      case RTI_implied:
        return "RTI";
      case EOR_zero_page_x_indirect:
        return "EOR";
      case EOR_zero_page:
        return "EOR";
      case LSR_zero_page:
        return "LSR";
      case RMB_zero_page_4:
        return "RMB";
      case PHA_implied:
        return "PHA";
      case EOR_immediate:
        return "EOR";
      case LSR_implied_a:
        return "LSR";
      case JMP_absolute:
        return "JMP";
      case EOR_absolute:
        return "EOR";
      case LSR_absolute:
        return "LSR";
      case BBR_relative_bit_branch_4:
        return "BBR";
      case BVS_relative:
        return "BVS";
      case EOR_zero_page_indirect_y:
        return "EOR";
      case EOR_zero_page_indirect:
        return "EOR";
      case EOR_zero_page_x:
        return "EOR";
      case LSR_zero_page_x:
        return "LSR";
      case RMB_zero_page_5:
        return "RMB";
      case CLI_implied:
        return "CLI";
      case EOR_absolute_y:
        return "EOR";
      case PHY_implied:
        return "PHY";
      case EOR_absolute_x:
        return "EOR";
      case LSR_absolute_x:
        return "LSR";
      case BBR_relative_bit_branch_5:
        return "BBR";
      case RTS_implied:
        return "RTS";
      case ADC_zero_page_x_indirect:
        return "ADC";
      case STZ_zero_page:
        return "STZ";
      case ADC_zero_page:
        return "ADC";
      case ROR_zero_page:
        return "ROR";
      case RMB_zero_page_6:
        return "RMB";
      case PLA_implied:
        return "PLA";
      case ADC_immediate:
        return "ADC";
      case ROR_implied:
        return "ROR";
      case JMP_absolute_indirect:
        return "JMP";
      case ADC_absolute:
        return "ADC";
      case ROR_absolute:
        return "ROR";
      case BBR_relative_bit_branch_6:
        return "BBR";
      case BVC_relative:
        return "BVC";
      case ADC_zero_page_indirect_y:
        return "ADC";
      case ADC_zero_page_indirect:
        return "ADC";
      case STZ_zero_page_x:
        return "STZ";
      case ADC_zero_page_x:
        return "ADC";
      case ROR_zero_page_x:
        return "ROR";
      case RMB_zero_page_7:
        return "RMB";
      case SEI_implied:
        return "SEI";
      case ADC_absolute_y:
        return "ADC";
      case PLY_implied:
        return "PLY";
      case JMP_absolute_x_indirect:
        return "JMP";
      case ADC_absolute_x:
        return "ADC";
      case ROR_absolute_x:
        return "ROR";
      case BBR_relative_bit_branch_7:
        return "BBR";
      case BRA_relative:
        return "BRA";
      case STA_zero_page_x_indirect:
        return "STA";
      case STY_zero_page:
        return "STY";
      case STA_zero_page:
        return "STA";
      case STX_zero_page:
        return "STX";
      case SMB_zero_page_0:
        return "SMB";
      case DEC_implied_y:
        return "DEC";
      case BIT_immediate_immediate:
        return "BIT";
      case TXA_implied:
        return "TXA";
      case STY_absolute:
        return "STY";
      case STA_absolute:
        return "STA";
      case STX_absolute:
        return "STX";
      case BBS_relative_bit_branch_0:
        return "BBS";
      case BCC_relative:
        return "BCC";
      case STA_zero_page_indirect_y:
        return "STA";
      case STA_zero_page_indirect:
        return "STA";
      case STY_zero_page_x:
        return "STY";
      case STA_zero_page_x:
        return "STA";
      case STX_zero_page_y:
        return "STX";
      case SMB_zero_page_1:
        return "SMB";
      case TYA_implied:
        return "TYA";
      case STA_absolute_y:
        return "STA";
      case TXS_implied:
        return "TXS";
      case STZ_absolute:
        return "STZ";
      case STA_absolute_x:
        return "STA";
      case STZ_absolute_x:
        return "STZ";
      case BBS_relative_bit_branch_1:
        return "BBS";
      case LDY_immediate:
        return "LDY";
      case LDA_zero_page_x_indirect:
        return "LDA";
      case LDX_immediate:
        return "LDX";
      case LDY_zero_page:
        return "LDY";
      case LDA_zero_page:
        return "LDA";
      case LDX_zero_page:
        return "LDX";
      case SMB_zero_page_2:
        return "SMB";
      case TAY_implied:
        return "TAY";
      case LDA_immediate:
        return "LDA";
      case TAX_implied:
        return "TAX";
      case LDY_absolute:
        return "LDY";
      case LDA_absolute:
        return "LDA";
      case LDX_absolute:
        return "LDX";
      case BBS_relative_bit_branch_2:
        return "BBS";
      case BCS_relative:
        return "BCS";
      case LDA_zero_page_indirect_y:
        return "LDA";
      case LDA_zero_page_indirect:
        return "LDA";
      case LDY_zero_page_x:
        return "LDY";
      case LDA_zero_page_x:
        return "LDA";
      case LDX_zero_page_y:
        return "LDX";
      case SMB_zero_page_3:
        return "SMB";
      case CLV_implied:
        return "CLV";
      case LDA_absolute_y:
        return "LDA";
      case TSX_implied:
        return "TSX";
      case LDY_absolute_x:
        return "LDY";
      case LDA_absolute_x:
        return "LDA";
      case LDX_absolute_y:
        return "LDX";
      case BBS_relative_bit_branch_3:
        return "BBS";
      case CPY_immediate:
        return "CPY";
      case CMP_zero_page_x_indirect:
        return "CMP";
      case CPY_zero_page:
        return "CPY";
      case CMP_zero_page:
        return "CMP";
      case DEC_zero_page:
        return "DEC";
      case SMB_zero_page_4:
        return "SMB";
      case INC_implied_y:
        return "INC";
      case CMP_immediate:
        return "CMP";
      case DEC_implied_x:
        return "DEC";
      case WAI_implied:
        return "WAI";
      case CPY_absolute:
        return "CPY";
      case CMP_absolute:
        return "CMP";
      case DEC_absolute:
        return "DEC";
      case BBS_relative_bit_branch_4:
        return "BBS";
      case BEQ_relative:
        return "BEQ";
      case CMP_zero_page_indirect_y:
        return "CMP";
      case CMP_zero_page_indirect:
        return "CMP";
      case CMP_zero_page_x:
        return "CMP";
      case DEC_zero_page_x:
        return "DEC";
      case SMB_zero_page_5:
        return "SMB";
      case CLD_implied:
        return "CLD";
      case CMP_absolute_y:
        return "CMP";
      case PHX_implied:
        return "PHX";
      case STP_implied:
        return "STP";
      case CMP_absolute_x:
        return "CMP";
      case DEC_absolute_x:
        return "DEC";
      case BBS_relative_bit_branch_5:
        return "BBS";
      case CPX_immediate:
        return "CPX";
      case SBC_zero_page_x_indirect:
        return "SBC";
      case CPX_zero_page:
        return "CPX";
      case SBC_zero_page:
        return "SBC";
      case INC_zero_page:
        return "INC";
      case SMB_zero_page_6:
        return "SMB";
      case INC_implied_x:
        return "INC";
      case SBC_immediate:
        return "SBC";
      case CPX_absolute:
        return "CPX";
      case SBC_absolute:
        return "SBC";
      case INC_absolute:
        return "INC";
      case BBS_relative_bit_branch_6:
        return "BBS";
      case BNE_relative:
        return "BNE";
      case SBC_zero_page_indirect_y:
        return "SBC";
      case SBC_zero_page_indirect:
        return "SBC";
      case SBC_zero_page_x:
        return "SBC";
      case INC_zero_page_x:
        return "INC";
      case SMB_zero_page_7:
        return "SMB";
      case SED_implied:
        return "SED";
      case SBC_absolute_y:
        return "SBC";
      case PLX_implied:
        return "PLX";
      case SBC_absolute_x:
        return "SBC";
      case INC_absolute_x:
        return "INC";
      case BBS_relative_bit_branch_7:
        return "BBS";
      case NOP_absolute:
      case NOP_implied:
      case (byte) 0xf4:
      case (byte) 0x54:
      case (byte) 0xd4:
      case (byte) 0xc2:
      case (byte) 0xe2:
      case (byte) 0xdc:
      case (byte) 0x02:
      case (byte) 0x22:
      case (byte) 0x62:
      case (byte) 0x82:
      case (byte) 0x42:
      case (byte) 0x44:
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
      case (byte) 0xfc:
        return "NOP";
    }
    return "";
  }

  private void clearOverflow()
  {
    if (cycle == 1)
    {
      programCounter--;
      setOverflow(false);
    }
    doneInstruction();
  }

  private void transferStackPointerToX()
  {
    if (cycle == 1)
    {
      programCounter--;
      xIndex = stack;
      updateZeroAndNegative(xIndex);
    }
    doneInstruction();
  }

  private void loadY_immediate()
  {
    if (cycle == 1)
    {
      updateZeroAndNegative(data);
      yIndex = data;
    }
    doneInstruction();
  }

  private void loadY_absolute_x()
  {
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
        updateZeroAndNegative(data);
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadY_zero_page_x()
  {
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
        updateZeroAndNegative(data);
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadY_absolute()
  {
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
        updateZeroAndNegative(data);
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadY_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        updateZeroAndNegative(data);
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadX_absolute_y()
  {
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
        loadX();
      }
      default:
        doneInstruction();
    }
  }

  private void loadX_zero_page_y()
  {
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
        loadX();
      }
      default:
        doneInstruction();
    }
  }

  private void loadX_absolute()
  {
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
        loadX();
      }
      default:
        doneInstruction();
    }
  }

  private void transferAccumulatorToY()
  {
    if (cycle == 1)
    {
      programCounter--;
      yIndex = accumulator;
      updateZeroAndNegative(yIndex);
    }
    doneInstruction();
  }

  private void loadX_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        loadX();
      }
      default:
        doneInstruction();
    }
  }

  private void loadX_immediate()
  {
    if (cycle == 1)
    {
      loadX();
    }
    doneInstruction();
  }

  private void loadAccumulator_absolute_x()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadAccumulator_absolute_y()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadAccumulator_zero_page_x()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void extracted_zero_page_indirect_y()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadAccumulator_absolute()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void transferAccumulatorToX()
  {
    if (cycle == 1)
    {
      programCounter--;
      xIndex = accumulator;
      updateZeroAndNegative(xIndex);
    }
    doneInstruction();
  }

  private void loadAccumulator_immediate()
  {
    if (cycle == 1)
    {
      updateZeroAndNegative(data);
      accumulator = data;
    }
    doneInstruction();
  }

  private void loadAccumulator_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void loadAccumulator_zero_page_x_indirect()
  {
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
        updateZeroAndNegative(data);
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void branchIfNotZero()
  {
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
    }
  }

  private void clearDecimalMode()
  {
    if (cycle == 1)
    {
      programCounter--;
      setDecimalMode(false);
    }
    doneInstruction();
  }

  private void invalid5()
  {
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
    }
  }

  private void stop()
  {
    if (cycle == 1)
    {
      programCounter--;
      stopped = true;
    }
    doneInstruction();
  }

  private void compareAccumulatorWithMemory_absolute_y()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_zero_page_x()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_zero_page_indirect()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_zero_page_indirect_y()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_absolute()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_immediate()
  {
    if (cycle == 1)
    {
      address = (short) (((data ^ 0xFF) & 0xFF) + (accumulator & 0xFF) + 1);
      updateCarry(address);
    }
    doneInstruction();
  }

  private void compareAccumulatorWithMemory_zero_page()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_zero_page_x_indirect()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void compareAccumulatorWithMemory_absolute_x()
  {
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
        updateCarry(address);
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_zero_page_x_indirect()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void compareXWithMemory_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        CompareXWithData();
      }
      default:
        doneInstruction();
    }
  }

  private void compareXWithMemory_immediate()
  {
    if (cycle == 1)
    {
      CompareXWithData();
    }
    doneInstruction();
  }

  private void compareXWithMemory_absolute()
  {
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
        CompareXWithData();
      }
      default:
        doneInstruction();
    }
  }

  private void setDecimalMode()
  {
    if (cycle == 1)
    {
      programCounter--;
      setDecimalMode(true);
    }
    doneInstruction();
  }

  private void pullXFromStack()
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        popFromStack();
        break;
      }
      case 2:
      {
        xIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void invalid4()
  {
    if (cycle == 1)
    {
      addressLowFromDataReadProgramCounter();
    }
    else if (cycle == 2)
    {
      addressHighFromDataOffsetAddressByX();
      doneInstruction();
    }
  }

  private void noOperation()
  {
    if (cycle == 1)
    {
      programCounter--;
    }
    doneInstruction();
  }

  private void invalid3()
  {
    if (cycle == 1)
    {
      address = dataLowByte();
    }
    doneInstruction();
  }

  private void incrementMemory_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
        break;
      }
      case 3:
      {
        wantRead(address);
        break;
      }
      case 4:
      {
        incrementMemory();
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void invalid2()
  {
    if (cycle == 1)
    {
      addressLowFromDataReadAddress();
    }
    else if (cycle == 2)
    {
      address = offsetAddressByX();
      doneInstruction();
    }
  }

  private void branchIfZero()
  {
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
    }
  }

  private void incrementMemory_absolute()
  {
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
        incrementMemory();
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void incrementX()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = xIndex;
      data++;
      updateZeroAndNegative(data);
      xIndex = data;
    }
    doneInstruction();
  }

  private void incrementMemory_zero_page()
  {
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
        incrementMemory();
        break;
      }
      case 4:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void incrementY()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = yIndex;
      data++;
      updateZeroAndNegative(data);
      yIndex = data;
    }
    doneInstruction();
  }

  private void incrementMemory_absolute_x()
  {
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
        incrementMemory();
        break;
      }
      case 6:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_absolute_x()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_absolute_y()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_zero_page_x()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_zero_page_indirect()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_zero_page_indirect_y()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_absolute()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_immediate()
  {
    switch (cycle)
    {
      case 1:
      {
        addWithCarryFromAccumulator(data ^ 0xFF);
        break;
      }
      case 2:
      {
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void subtractWithCarry_zero_page()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressNotTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void transferXToStackPointer()
  {
    if (cycle == 1)
    {
      programCounter--;
      stack = xIndex;
    }
    doneInstruction();
  }

  private void transferYtoAccumulator()
  {
    if (cycle == 1)
    {
      programCounter--;
      accumulator = yIndex;
      updateZeroAndNegative(accumulator);
    }
    doneInstruction();
  }

  private void transferXToAccumulator()
  {
    if (cycle == 1)
    {
      programCounter--;
      accumulator = xIndex;
      updateZeroAndNegative(accumulator);
    }
    doneInstruction();
  }

  private void branchIfNotCarry()
  {
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
    }
  }

  private void setMemoryBit_zero_page(int bit)
  {
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
        data |= 1 << bit;
        wantWrite(address, data);
        break;
      }
      case 4:
      {
      }
      default:
        doneInstruction();
    }
  }

  private void storeX_zero_page_y()
  {
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
    }
  }

  private void storeX_absolute()
  {
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
    }
  }

  private void storeX_zero_page()
  {
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
    }
  }

  private void storeY_zero_page_x()
  {
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
    }
  }

  private void storeY_absolute()
  {
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
    }
  }

  private void storeY_zero_page()
  {
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
    }
  }

  private void pullAccumulatorFromStack()
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        popFromStack();
        break;
      }
      case 2:
      {
        accumulator = data;
      }
      default:
        doneInstruction();
    }
  }

  private void branchIfOverflow()
  {
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
    }
  }

  private void setInterrupt()
  {
    if (cycle == 1)
    {
      programCounter--;
      setInterrupt(true);
    }
    doneInstruction();
  }

  private void storeAccumulator_absolute_x()
  {
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
    }
  }

  private void storeAccumulator_absolute_y()
  {
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
    }
  }

  private void storeAccumulator_zero_page_x()
  {
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
    }
  }

  private void storeAccumulator_zero_page_indirect()
  {
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
    }
  }

  private void storeAccumulator_zero_page_indirect_y()
  {
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
    }
  }

  private void storeAccumulator_absolute()
  {
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
    }
  }

  private void storeAccumulator_zero_page()
  {
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
    }
  }

  private void storeAccumulator_zero_page_x_indirect()
  {
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
    }
  }

  private void branchAlways()
  {
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
    }
  }

  private void rotateRight_absolute_x()
  {
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
    }
  }

  private void rotateRight_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
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
    }
  }

  private void rotateRight_absolute()
  {
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
    }
  }

  private void rotateRight_implied()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = accumulator;
      short result = (short) (((data & 0xFF) >> 1) | (isCarry() ? 0x80 : 0));
      updateCarryStatus();
      data = (byte) result;
      updateZeroAndNegative(result);
      accumulator = data;
    }
    doneInstruction();
  }

  private void rotateRight_zero_page()
  {
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
    }
  }

  private void storeZero_absolute_x()
  {
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
    }
  }

  private void storeZero_absolute()
  {
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
    }
  }

  private void storeZero_zero_page_x()
  {
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
    }
  }

  private void storeZero_zero_page()
  {
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
    }
  }

  private void returnFromSubroutine_implied()
  {
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
        popFromStack();
        break;
      }
      case 4:
      {
        programCounter = dataLowByte();
        popFromStack();
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
    }
  }

  private void noOperation_absolute()
  {
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
    }
  }

  private void pushYOntoStack()
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        pushToStack(yIndex);
        break;
      }
      case 2:
      {
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void clearInterrupt()
  {
    if (cycle == 1)
    {
      programCounter--;
      setInterrupt(false);
    }
    doneInstruction();
  }

  private void branchIfNotOverflow()
  {
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
    }
  }

  private void addWithCarry_absolute_y()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void accumulatorFromAddressSetOverflowAndCarry(int i)
  {
    setOverflow((i) != 0);
    updateCarry(address);
    accumulator = (byte) address;
  }

  private void addWithCarry_zero_page_x()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_zero_page_indirect()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_zero_page_indirect_y()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_absolute()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_immediate()
  {
    switch (cycle)
    {
      case 1:
      {
        addWithCarryFromAccumulator(data);
        break;
      }
      case 2:
      {
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_zero_page()
  {
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
        updateCarry(address);
        accumulator = (byte) address;
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_zero_page_x_indirect()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void addWithCarry_absolute_x()
  {
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
        accumulatorFromAddressSetOverflowAndCarry(xorAddressTestHighBit());
      }
      default:
        doneInstruction();
    }
  }

  private void pullYFromStack()
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        popFromStack();
        break;
      }
      case 2:
      {
        yIndex = data;
      }
      default:
        doneInstruction();
    }
  }

  private void jump_absolute_x_indirect()
  {
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
        setProgramCounterFromAddress();
      }
      default:
        doneInstruction();
    }
  }

  private void jump_absolute_indirect()
  {
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
        setProgramCounterFromAddress();
      }
      default:
        doneInstruction();
    }
  }

  private void jump_absolute()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadProgramCounter();
        break;
      }
      case 2:
      {
        setProgramCounterFromAddress();
      }
      default:
        doneInstruction();
    }
  }

  private void logicalShiftRight_absolute_x()
  {
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
    }
  }

  private void logicalShiftRight_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
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
    }
  }

  private void logicalShiftRight_absolute()
  {
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
    }
  }

  private void logicalShiftRight_zero_page()
  {
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
    }
  }

  private void exclusiveOrAccumulator_absolute_x()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_absolute_y()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_zero_page_x()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void invalid_1()
  {
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
    }
  }

  private void exclusiveOrAccumulator_zero_page_indirect()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_zero_page_indirect_y()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_absolute()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_immediate()
  {
    if (cycle == 1)
    {
      accumulator ^= data;
      updateZeroAndNegative(accumulator);
    }
    doneInstruction();
  }

  private void exclusiveOrAccumulator_zero_page()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void exclusiveOrAccumulator_zero_page_x_indirect()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void returnFromInterrupt()
  {
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
        popFromStack();
        break;
      }
      case 3:
      {
        processorStatus = data;
        popFromStack();
        break;
      }
      case 4:
      {
        programCounter = dataLowByte();
        popFromStack();
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
    }
  }

  private void rotateLeft_absolute_x()
  {
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
        rotateLeftWithCarry();
        break;
      }
      case 6:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void logicalShiftRight_implied()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = accumulator;
      updateCarryStatus();
      short result = (short) ((data & 0xFF) >> 1);
      data = (byte) result;
      updateZeroAndNegative(result);
      accumulator = data;
    }
    doneInstruction();
  }

  private void rotateLeftWithCarry()
  {
    short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
    data = (byte) result;
    updateCarry(result);
    wantWrite(address, data);
  }

  private void branchIfBitSet(int i)
  {
    switch (cycle)
    {
      case 1:
      {
        addressFromDataReadPC();
        break;
      }
      case 2:
      {
        takeBranchOnDataBitSet(i);
      }
      default:
        doneInstruction();
    }
  }

  private void testBits_immediate_immediate()
  {
    if (cycle == 1)
    {
      updateZeroStatus();
    }
    doneInstruction();
  }

  private void testBits_absolute_x()
  {
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
        updateProcessStateFromSomeWeirdDataThing();
        updateZeroStatus();
      }
      default:
        doneInstruction();
    }
  }

  private void decrement_absolute_x()
  {
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
        updateZeroAndNegative(data);
        wantWrite(address, data);
        break;
      }
      case 6:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void decrement_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
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
        updateZeroAndNegative(data);
        wantWrite(address, data);
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void decrement_absolute()
  {
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
        updateZeroAndNegative(data);
        wantWrite(address, data);
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void decrement_implied_x()
  {
    if (cycle == 1)
    {
      DecrementX();
    }
    doneInstruction();
  }

  private void decrement_zero_page()
  {
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
        updateZeroAndNegative(data);
        wantWrite(address, data);
        break;
      }
      case 4:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void decrement_implied_y()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = yIndex;
      data--;
      updateZeroAndNegative(data);
      yIndex = data;
    }
    doneInstruction();
  }

  private void decrement_implied_a()
  {
    if (cycle == 1)
    {
      decrementAccumulator();
    }
    doneInstruction();
  }

  private void andAccumulator_absolute_x()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void testBits_zero_page_x()
  {
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
        updateProcessStateFromSomeWeirdDataThing();
        updateZeroStatus();
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_zero_page_indirect()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_zero_page_indirect_y()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void branchIfNegative()
  {
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
    }
  }

  private void rotateLeft_absolute()
  {
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
        rotateLeftWithCarry();
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_absolute()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void testBits_absolute()
  {
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
        updateProcessStateFromSomeWeirdDataThing();
        updateZeroStatus();
      }
      default:
        doneInstruction();
    }
  }

  private void rotateLeft_implied()
  {
    if (cycle == 1)
    {
      programCounter--;
      data = accumulator;
      short result = (short) (((data & 0xFF) << 1) | ((isCarry()) ? 1 : 0));
      data = (byte) result;
      updateCarry(result);
      accumulator = data;
    }
    doneInstruction();
  }

  private void andAccumulator_immediate()
  {
    if (cycle == 1)
    {
      accumulator &= data;
      updateZeroAndNegative(accumulator);
    }
    doneInstruction();
  }

  private void pullProcessorStatusFromStack_implied()
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        popFromStack();
        break;
      }
      case 2:
      {
        processorStatus = data;
      }
      default:
        doneInstruction();
    }
  }

  private void rotateLeft_zero_page()
  {
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
        rotateLeftWithCarry();
        break;
      }
      case 4:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_zero_page()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void testBits_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        updateProcessStateFromSomeWeirdDataThing();
        updateZeroStatus();
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_zero_page_x()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void andAccumulator_absolute_y()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void rotateLeft_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
        break;
      }
      case 3:
      {
        wantRead(address);
        break;
      }
      case 4:
      {
        rotateLeftWithCarry();
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void setCarry()
  {
    if (cycle == 1)
    {
      programCounter--;
      setCarry(true);
    }
    doneInstruction();
  }

  private void updateProcessStateFromSomeWeirdDataThing()
  {
    processorStatus = (byte) ((processorStatus & 0x3F) | (data & 0xC0));
  }

  private void andAccumulator_zero_page_x_indirect()
  {
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
        updateZeroAndNegative(accumulator);
      }
      default:
        doneInstruction();
    }
  }

  private void jumpToSubroutine()
  {
    switch (cycle)
    {
      case 1:
      {
        address = dataLowByte();
        pushToStack(programCounterHighByte());
        break;
      }
      case 2:
      {
        pushToStack(programCounterLowByte());
        break;
      }
      case 3:
      {
        wantRead(programCounter++);
        break;
      }
      case 4:
      {
        setProgramCounterFromAddress();
      }
      default:
        doneInstruction();
    }
  }

  private void arithmeticShiftLeft_absolute_x()
  {
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
    }
  }

  private void orAccumulatorWithMemory_absolute_x()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void testAndResetBits_absolute()
  {
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
    }
  }

  private void incrementAccumulator()
  {
    if (cycle == 1)
    {
      incrementAccumulatorAndUpdateZeroAndNegative();
    }
    doneInstruction();
  }

  private void orAccumulatorWithMemory_absolute_y()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void clearCarry()
  {
    if (cycle == 1)
    {
      programCounter--;
      setCarry(false);
    }
    doneInstruction();
  }

  private void arithmeticShiftLeft_zero_page_x()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        readAddressOffsetByXSetMLB();
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
    }
  }

  private void orAccumulatorWithMemory_zero_page_x()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void testAndResetBits_zero_page()
  {
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
    }
  }

  private void orAccumulatorWithMemory_zero_page_indirect()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void orAccumulatorWithMemory_zero_page_indirect_y()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void branchIfPositive()
  {
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
    }
  }

  private void branchIfBitNotSet(int i)
  {
    switch (cycle)
    {
      case 1:
      {
        addressFromDataReadPC();
        break;
      }
      case 2:
      {
        takeBranchOnDataBitNotSet(i);
      }
      default:
        doneInstruction();
    }
  }

  private void arithmeticShiftLeft_absolute()
  {
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
    }
  }

  private void orAccumulatorWithMemory_absolute()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void testAndSetBits_absolute()
  {
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
        orDataWithAccumulatorUpdateZeroWriteToMemory();
        break;
      }
      case 5:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void arithmeticShiftLeftAccumulator()
  {
    if (cycle == 1)
    {
      accumulatorShiftLeft();
    }
    doneInstruction();
  }

  private void orAccumulatorWithMemory_immediate()
  {
    if (cycle == 1)
    {
      orAccumulatorWithDataUpdateZeroAndNegative();
    }
    doneInstruction();
  }

  private void push(byte value)
  {
    switch (cycle)
    {
      case 1:
      {
        programCounter--;
        pushToStack(value);
        break;
      }
      case 2:
      {
      }
      default:
        doneInstruction();
    }
  }

  private void clearBit(int bit)
  {
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
        data &= ~(1 << bit);
        wantWrite(address, data);
        break;
      }
      case 4:
      {
      }
      default:
        doneInstruction();
    }
  }

  private void arithmeticShiftLeft_zero_page()
  {
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
    }
  }

  private void orAccumulatorWithMemory_zero_page()
  {
    switch (cycle)
    {
      case 1:
      {
        addressLowFromDataReadAddress();
        break;
      }
      case 2:
      {
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void testAndSetMemoryBitsAgainstAccumulator_zero_page()
  {
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
        orDataWithAccumulatorUpdateZeroWriteToMemory();
        break;
      }
      case 4:
      {
        setMemoryLock(false);
      }
      default:
        doneInstruction();
    }
  }

  private void orAccumulatorWithMemory_zero_page_x_indirect()
  {
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
        orAccumulatorWithDataUpdateZeroAndNegative();
      }
      default:
        doneInstruction();
    }
  }

  private void break_()
  {
    switch (cycle)
    {
      case 1:
      {
        pushToStack(programCounterHighByte());
        break;
      }
      case 2:
      {
        pushToStack(programCounterLowByte());
        break;
      }
      case 3:
      {
        pushToStack((byte) isBreakBit());
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
        throw new RuntimeException("break_");
//        vectorToPull = IRQ_VECTOR;
//        break;
      }
      case 6:
      {
        programCounter |= data << 8;
        wantVPB(false);
      }
      default:
        doneInstruction();
    }
  }

  private void readAddressOffsetByXSetMLB()
  {
    address = offsetAddressByX();
    setMemoryLock(true);
    wantRead(address);
  }

  private void CompareXWithData()
  {
    address = (short) (((data ^ 0xFF) & 0xFF) + (xIndex & 0xFF) + 1);
    updateCarry(address);
  }

  private void decrementAccumulator()
  {
    programCounter--;
    data = accumulator;
    data--;
    updateZeroAndNegative(data);
    accumulator = data;
  }

  private void DecrementX()
  {
    programCounter--;
    data = xIndex;
    data--;
    updateZeroAndNegative(data);
    xIndex = data;
  }

  private void loadX()
  {
    updateZeroAndNegative(data);
    xIndex = data;
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

  private void setProgramCounterFromAddress()
  {
    address |= (short) (data << 8);
    programCounter = address;
  }

  private void incrementAccumulatorAndUpdateZeroAndNegative()
  {
    programCounter--;
    data = accumulator;
    data++;
    updateZeroAndNegative(data);
    accumulator = data;
  }

  private void incrementMemory()
  {
    data++;
    updateZeroAndNegative(data);
    wantWrite(address, data);
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

  private void addressFromDataReadPC()
  {
    address = data;
    wantRead(programCounter++);
  }

  private void rotateRight()
  {
    short result = (short) (((data & 0xFF) >> 1) | (isCarry() ? 0x80 : 0));
    updateCarryStatus();
    data = (byte) result;
    updateZeroAndNegative(result);
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
    updateZeroAndNegative(result);
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
    updateCarry(result);
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

  private void orAccumulatorWithDataUpdateZeroAndNegative()
  {
    accumulator |= data;
    updateZeroAndNegative(accumulator);
  }

  private void addressLowFromDataReadProgramCounter()
  {
    address = dataLowByte();
    wantRead(programCounter++);
  }

  private void orDataWithAccumulatorUpdateZeroWriteToMemory()
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
    updateCarry(result);
    wantWrite(address, data);
  }

  protected void updateZeroAndNegative(int result)
  {
    setZeroStatus((result & 0xFF) == 0);
    setNegative((result & 0x80) != 0);
  }

  protected void updateCarry(int value)
  {
    updateZeroAndNegative(value);
    setCarry((value & 0x100) != 0);
  }

  protected void setNegative(boolean negative)
  {
    if (negative)
    {
      processorStatus |= P_N_BIT;
    }
    else
    {
      processorStatus &= ~P_N_BIT;
    }
  }

  protected void setZeroStatus(boolean zeroStatus)
  {
    if (zeroStatus)
    {
      processorStatus |= P_Z_BIT;
    }
    else
    {
      processorStatus &= ~P_Z_BIT;
    }
  }

  protected void setCarry(boolean carry)
  {
    if (carry)
    {
      processorStatus |= P_C_BIT;
    }
    else
    {
      processorStatus &= ~P_C_BIT;
    }
  }

  public void reset(InstanceState cis)
  {
    stopped = false;
    fetchedOpcode = BRK;
    cycle = 1;
    setInterrupt(true);
    setDecimalMode(false);
    previousNMI = true;
    vectorToPull = RESET_VECTOR;
    wantingVectorPull = false;
    parent.setVPB(cis, false);
    wantingSync = false;
    parent.setSync(cis, false);
    parent.setMLB(cis, false);
    parent.setReady(cis, true);
  }

  protected void wantRead(short address)
  {
    intendedAddress = address;
    intendedRWB = true;
  }

  protected void wantWrite(short address, byte data)
  {
    intendedAddress = address;
    intendedData = data;
    intendedRWB = false;
  }

  protected void pushToStack(byte value)
  {
    if (vectorToPull == RESET_VECTOR)
    {
      wantRead(stackAddress());
    }
    else
    {
      wantWrite(stackAddress(), value);
    }
    stack--;
  }

  protected void popFromStack()
  {
    stack++;
    wantRead(stackAddress());
  }

  private short stackAddress()
  {
    return (short) (0x0100 | (stack & 0xFF));
  }

  protected void wantVPB(boolean want)
  {
    wantingVectorPull = want;
  }

  public void tick(InstanceState instanceState, boolean isResetting, boolean clock)
  {
    if (isResetting)
    {
      reset(instanceState);
    }

    boolean isReady = parent.isReady(instanceState);
    if (!isReady && !stopped && fetchedOpcode == WAI_implied && cycle == 2)
    {
      parent.setReady(instanceState, true);
    }
    if (stopped || !isReady)
    {
      return;
    }

    this.instanceState = instanceState;
    boolean clockFallingEdge = !clock && previousClock;
    boolean clockRisingEdge = clock && !previousClock;

    if (clockFallingEdge)
    {
      updateOverflow(instanceState);

      do
      {
        wantingSync = cycle < 0;
        if (cycle < 0)
        {
          doneInstructionUpdateAddress();
        }
        else if (cycle == 0)
        {
          fetchNextOpcodeOrInterrupt(instanceState);
        }
        else
        {
          fetchData(instanceState);
        }
      }
      while (cycle < 0 && !stopped);
    }
    else if (clockRisingEdge)
    {
      applyOutputs(instanceState);
    }

    this.instanceState = null;
    this.previousClock = clock;
  }

  private void updateOverflow(InstanceState instanceState)
  {
    boolean overflow = parent.isOverflow(instanceState);
    if (overflow != previousOverflow)
    {
      if (overflow)
      {
        processorStatus |= P_V_BIT;
      }
      previousOverflow = overflow;
    }
  }

  private void doneInstructionUpdateAddress()
  {
    wantRead(programCounter);
    programCounter++;
    cycle++;
  }

  private void fetchData(InstanceState instanceState)
  {
    data = parent.getDataFromPort(instanceState);
    doInstruction();
    if (cycle >= 0)
    {
      cycle++;
    }
  }

  private void fetchNextOpcodeOrInterrupt(InstanceState instanceState)
  {
    wantRead(programCounter);
    programCounter++;

    boolean nmi = parent.isNonMaskableInterrupt(instanceState);
    if (nmi && !previousNMI)
    {
      throw new RuntimeException("nmi && !previousNMI");
//            vectorToPull = NMI_VECTOR;
//            programCounter -= 2;
//            fetchedOpcode = BRK;
    }
    else if (parent.isInterruptRequest(instanceState) && isInterrupt() == 0)
    {
      throw new RuntimeException("parent.isInterruptRequest(instanceState) && isInterrupt() == 0");
//            vectorToPull = IRQ_VECTOR;
//            programCounter -= 2;
//            fetchedOpcode = BRK;
    }
    else
    {
      fetchedOpcode = parent.getDataFromPort(instanceState);
    }
    previousNMI = nmi;
    cycle++;
  }

  private void applyOutputs(InstanceState instanceState)
  {
    if (intendedRWB)
    {
      parent.doRead(instanceState, intendedAddress);
    }
    else
    {
      parent.doWrite(instanceState, intendedAddress, intendedData);
    }
    parent.setSync(instanceState, wantingSync);
    parent.setVPB(instanceState, wantingVectorPull);
  }

  private int isInterrupt()
  {
    return processorStatus & P_I_BIT;
  }

  protected void updateZeroStatus()
  {
    setZeroStatus((data & accumulator) == 0);
  }

  protected void setOverflow(boolean overflow)
  {
    if (overflow)
    {
      processorStatus |= P_V_BIT;
    }
    else
    {
      processorStatus &= ~P_V_BIT;
    }
  }

  protected void setDecimalMode(boolean decimalMode)
  {
    if (decimalMode)
    {
      processorStatus |= P_D_BIT;
    }
    else
    {
      processorStatus &= ~P_D_BIT;
    }
  }

  protected void setInterrupt(boolean interrupt)
  {
    if (interrupt)
    {
      processorStatus |= P_I_BIT;
    }
    else
    {
      processorStatus &= ~P_I_BIT;
    }
  }
}

