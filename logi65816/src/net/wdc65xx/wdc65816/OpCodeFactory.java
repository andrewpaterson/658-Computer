package net.wdc65xx.wdc65816;

import net.util.EmulatorException;
import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;
import net.wdc65xx.wdc65816.interrupt.*;
import net.wdc65xx.wdc65816.opcode.OpCode;

import java.util.ArrayList;
import java.util.List;

import static net.wdc65xx.wdc65816.OpCodeName.*;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;
import static net.wdc65xx.wdc65816.WidthFromRegister.XY;
import static net.wdc65xx.wdc65816.addressingmode.InstructionCycleFactory.*;

@SuppressWarnings({"SameParameterValue"})
public class OpCodeFactory
{
  public static OpCode[] createOpcodes()
  {
    List<OpCode> opCodes = new ArrayList<>();

    add(opCodes, createBrk(BRK_Interrupt, createStackSoftwareInterruptCycles(new BRKVector(), Cpu65816::BRK)));
    add(opCodes, createORA(ORA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::ORA)));
    add(opCodes, createCOP(COP_Interrupt, createStackSoftwareInterruptCycles(new COPVector(), Cpu65816::COP)));
    add(opCodes, createORA(ORA_StackRelative, createStackRelativeCycles(Cpu65816::ORA)));
    add(opCodes, createTSB(TSB_DirectPage, createDirectRMWCycles(Cpu65816::TSB)));
    add(opCodes, createORA(ORA_DirectPage, createDirectCycles(Cpu65816::ORA, M)));
    add(opCodes, createASL(ASL_DirectPage, createDirectRMWCycles(Cpu65816::ASL)));
    add(opCodes, createORA(ORA_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::ORA)));
    add(opCodes, createPHP(PHP_StackImplied, createStackImpliedPHPCycles(Cpu65816::PHP)));
    add(opCodes, createORA(ORA_Immediate, createImmediateCycles(Cpu65816::ORA, M)));
    add(opCodes, createSLA(ASL_Accumulator, createAccumulatorCycles(Cpu65816::ASL_A)));
    add(opCodes, createPHD(PHD_StackImplied, createStackImpliedPHDCycles(Cpu65816::PHD)));
    add(opCodes, createTSB(TSB_Absolute, createAbsoluteRMWCycles(Cpu65816::TSB)));
    add(opCodes, createORA(ORA_Absolute, createAbsoluteCycles(Cpu65816::ORA, M)));
    add(opCodes, createASL(ASL_Absolute, createAbsoluteRMWCycles(Cpu65816::ASL)));
    add(opCodes, createORA(ORA_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::ORA)));
    add(opCodes, createBPL(BPL_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BPL)));
    add(opCodes, createORA(ORA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::ORA)));
    add(opCodes, createORA(ORA_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::ORA)));
    add(opCodes, createORA(ORA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::ORA)));
    add(opCodes, createTRB(TRB_DirectPage, createDirectRMWCycles(Cpu65816::TRB)));
    add(opCodes, createORA(ORA_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::ORA, M)));
    add(opCodes, createASL(ASL_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::ASL)));
    add(opCodes, createORA(ORA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::ORA)));
    add(opCodes, createCLC(CLC_Implied, createImpliedCycles(Cpu65816::CLC)));
    add(opCodes, createORA(ORA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::ORA, M)));
    add(opCodes, createINA(INC_Accumulator, createAccumulatorCycles(Cpu65816::INC_A)));
    add(opCodes, createTCS(TCS_Implied, createImpliedCycles(Cpu65816::TCS)));
    add(opCodes, createTRB(TRB_Absolute, createAbsoluteRMWCycles(Cpu65816::TRB)));
    add(opCodes, createORA(ORA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::ORA, M)));
    add(opCodes, createASL(ASL_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::ASL)));
    add(opCodes, createORA(ORA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::ORA)));
    add(opCodes, createJSR(JSR_Absolute, createAbsoluteJSRCycles()));
    add(opCodes, createAND(AND_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::AND)));
    add(opCodes, createJSL(JSL_AbsoluteLong, createAbsoluteLongJSLCycles()));
    add(opCodes, createAND(AND_StackRelative, createStackRelativeCycles(Cpu65816::AND)));
    add(opCodes, createBIT(BIT_DirectPage, createDirectCycles(Cpu65816::BIT, M)));
    add(opCodes, createAND(AND_DirectPage, createDirectCycles(Cpu65816::AND, M)));
    add(opCodes, createROL(ROL_DirectPage, createDirectRMWCycles(Cpu65816::ROL)));
    add(opCodes, createAND(AND_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::AND)));
    add(opCodes, createPLP(PLP_StackImplied, createStackPLPCycles(Cpu65816::PLP)));
    add(opCodes, createAND(AND_Immediate, createImmediateCycles(Cpu65816::AND, M)));
    add(opCodes, createRLA(ROL_Accumulator, createAccumulatorCycles(Cpu65816::ROL_A)));
    add(opCodes, createPLD(PLD_StackImplied, createStackPLDCycles(Cpu65816::PLD)));
    add(opCodes, createBIT(BIT_Absolute, createAbsoluteCycles(Cpu65816::BIT, M)));
    add(opCodes, createAND(AND_Absolute, createAbsoluteCycles(Cpu65816::AND, M)));
    add(opCodes, createROL(ROL_Absolute, createAbsoluteRMWCycles(Cpu65816::ROL)));
    add(opCodes, createAND(AND_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::AND)));
    add(opCodes, createBMI(BMI_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BMI)));
    add(opCodes, createAND(AND_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::AND)));
    add(opCodes, createAND(AND_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::AND)));
    add(opCodes, createAND(AND_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::AND)));
    add(opCodes, createBIT(BIT_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::BIT, M)));
    add(opCodes, createAND(AND_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::AND, M)));
    add(opCodes, createROL(ROL_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::ROL)));
    add(opCodes, createAND(AND_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::AND)));
    add(opCodes, createSEC(SEC_Implied, createImpliedCycles(Cpu65816::SEC)));
    add(opCodes, createAND(AND_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::AND, M)));
    add(opCodes, createDEA(DEC_Accumulator, createAccumulatorCycles(Cpu65816::DEC_A)));
    add(opCodes, createTSC(TSC_Implied, createImpliedCycles(Cpu65816::TSC)));
    add(opCodes, createBIT(BIT_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::BIT, M)));
    add(opCodes, createAND(AND_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::AND, M)));
    add(opCodes, createROL(ROL_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::ROL)));
    add(opCodes, createAND(AND_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::AND)));
    add(opCodes, createRTI(RTI_StackImplied, createStackRTICycles(Cpu65816::RTI)));
    add(opCodes, createEOR(EOR_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::EOR)));
    add(opCodes, createWDM(WDM_Implied, createImpliedCycles(Cpu65816::WDM)));
    add(opCodes, createEOR(EOR_StackRelative, createStackRelativeCycles(Cpu65816::EOR)));
    add(opCodes, createMVP(MVP_BlockMove, createBlockMoveCycles(Cpu65816::MVP)));
    add(opCodes, createEOR(EOR_DirectPage, createDirectCycles(Cpu65816::EOR, M)));
    add(opCodes, createLSR(LSR_DirectPage, createDirectRMWCycles(Cpu65816::LSR)));
    add(opCodes, createEOR(EOR_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::EOR)));
    add(opCodes, createPHA(PHA_StackImplied, createStackPushCycles(Cpu65816::PHA, M)));
    add(opCodes, createEOR(EOR_Immediate, createImmediateCycles(Cpu65816::EOR, M)));
    add(opCodes, createSRA(LSR_Accumulator, createAccumulatorCycles(Cpu65816::LSR_A)));
    add(opCodes, createPHK(PHK_StackImplied, createStackPHKCycles(Cpu65816::PHK)));
    add(opCodes, createJMP(JMP_Absolute, createAbsoluteJMPCycles()));
    add(opCodes, createEOR(EOR_Absolute, createAbsoluteCycles(Cpu65816::EOR, M)));
    add(opCodes, createLSR(LSR_Absolute, createAbsoluteRMWCycles(Cpu65816::LSR)));
    add(opCodes, createEOR(EOR_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::EOR)));
    add(opCodes, createBVC(BVC_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BVC)));
    add(opCodes, createEOR(EOR_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::EOR)));
    add(opCodes, createEOR(EOR_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::EOR)));
    add(opCodes, createEOR(EOR_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::EOR)));
    add(opCodes, createMVN(MVN_BlockMove, createBlockMoveCycles(Cpu65816::MVN)));
    add(opCodes, createEOR(EOR_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::EOR, M)));
    add(opCodes, createLSR(LSR_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::LSR)));
    add(opCodes, createEOR(EOR_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::EOR)));
    add(opCodes, createCLI(CLI_Implied, createImpliedCycles(Cpu65816::CLI)));
    add(opCodes, createEOR(EOR_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::EOR, M)));
    add(opCodes, createPHY(PHY_StackImplied, createStackPushCycles(Cpu65816::PHY, XY)));
    add(opCodes, createTCD(TCD_Implied, createImpliedCycles(Cpu65816::TCD)));
    add(opCodes, createJMP(JML_AbsoluteLong, createAbsoluteLongJMLCycles()));
    add(opCodes, createEOR(EOR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::EOR, M)));
    add(opCodes, createLSR(LSR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::LSR)));
    add(opCodes, createEOR(EOR_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::EOR)));
    add(opCodes, createRTS(RTS_StackImplied, createStackRTSCycles()));
    add(opCodes, createADC(ADC_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::ADC)));
    add(opCodes, createPER(PER_StackProgramCounterRelativeLong, createStackPERCycles(Cpu65816::PER)));
    add(opCodes, createADC(ADC_StackRelative, createStackRelativeCycles(Cpu65816::ADC)));
    add(opCodes, createSTZ(STZ_DirectPage, createDirectWriteCycles(Cpu65816::STZ, M)));
    add(opCodes, createADC(ADC_DirectPage, createDirectCycles(Cpu65816::ADC, M)));
    add(opCodes, createROR(ROR_DirectPage, createDirectRMWCycles(Cpu65816::ROR)));
    add(opCodes, createADC(ADC_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::ADC)));
    add(opCodes, createPLA(PLA_Stack, createStackPullCycles(Cpu65816::PLA, M)));
    add(opCodes, createADC(ADC_Immediate, createImmediateCycles(Cpu65816::ADC, M)));
    add(opCodes, createRRA(ROR_Accumulator, createAccumulatorCycles(Cpu65816::ROR_A)));
    add(opCodes, createRTL(RTL_StackImplied, createStackRTLCycles()));
    add(opCodes, createJMP(JMP_AbsoluteIndirect, createAbsoluteIndirectJMPCycles()));
    add(opCodes, createADC(ADC_Absolute, createAbsoluteCycles(Cpu65816::ADC, M)));
    add(opCodes, createROR(ROR_Absolute, createAbsoluteRMWCycles(Cpu65816::ROR)));
    add(opCodes, createADC(ADC_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::ADC)));
    add(opCodes, createBVS(BVS_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BVS)));
    add(opCodes, createADC(ADC_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::ADC)));
    add(opCodes, createADC(ADC_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::ADC)));
    add(opCodes, createADC(ADC_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::ADC)));
    add(opCodes, createSTZ(STZ_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(Cpu65816::STZ, M)));
    add(opCodes, createADC(ADC_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::ADC, M)));
    add(opCodes, createROR(ROR_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::ROR)));
    add(opCodes, createADC(ADC_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::ADC)));
    add(opCodes, createSEI(SEI_Implied, createImpliedCycles(Cpu65816::SEI)));
    add(opCodes, createADC(ADC_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::ADC, M)));
    add(opCodes, createPLY(PLY_StackImplied, createStackPullCycles(Cpu65816::PLY, XY)));
    add(opCodes, createTDC(TDC_Implied, createImpliedCycles(Cpu65816::TDC)));
    add(opCodes, createJMP(JMP_AbsoluteIndexedIndirectWithX, createAbsoluteIndexedIndirectWithXJMPCycles()));
    add(opCodes, createADC(ADC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::ADC, M)));
    add(opCodes, createROR(ROR_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::ROR)));
    add(opCodes, createADC(ADC_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::ADC)));
    add(opCodes, createBRA(BRA_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BRA)));
    add(opCodes, createSTA(STA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXWriteCycles(Cpu65816::STA)));
    add(opCodes, createBRL(BRL_ProgramCounterRelativeLong, createRelativeLongCycles(Cpu65816::BRA)));
    add(opCodes, createSTA(STA_StackRelative, createStackRelativeWriteCycles(Cpu65816::STA)));
    add(opCodes, createSTY(STY_DirectPage, createDirectWriteCycles(Cpu65816::STY, XY)));
    add(opCodes, createSTA(STA_DirectPage, createDirectWriteCycles(Cpu65816::STA, M)));
    add(opCodes, createSTX(STX_DirectPage, createDirectWriteCycles(Cpu65816::STX, XY)));
    add(opCodes, createSTA(STA_DirectPageIndirectLong, createDirectIndirectLongWriteCycles(Cpu65816::STA)));
    add(opCodes, createDEY(DEY_Implied, createImpliedCycles(Cpu65816::DEY)));
    add(opCodes, createBIT(BIT_Immediate, createImmediateCycles(Cpu65816::BIT_I, M)));
    add(opCodes, createTXA(TXA_Implied, createImpliedCycles(Cpu65816::TXA)));
    add(opCodes, createPHB(PHB_StackImplied, createStackPHBCycles(Cpu65816::PHB)));
    add(opCodes, createSTY(STY_Absolute, createAbsoluteWriteCycles(Cpu65816::STY, XY)));
    add(opCodes, createSTA(STA_Absolute, createAbsoluteWriteCycles(Cpu65816::STA, M)));
    add(opCodes, createSTX(STX_Absolute, createAbsoluteWriteCycles(Cpu65816::STX, XY)));
    add(opCodes, createSTA(STA_AbsoluteLong, createAbsoluteLongWriteCycles(Cpu65816::STA)));
    add(opCodes, createBCC(BCC_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BCC)));
    add(opCodes, createSTA(STA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYWriteCycles(Cpu65816::STA)));
    add(opCodes, createSTA(STA_DirectPageIndirect, createDirectIndirectWriteCycles(Cpu65816::STA)));
    add(opCodes, createSTA(STA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYWriteCycles(Cpu65816::STA)));
    add(opCodes, createSTY(STY_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(Cpu65816::STY, XY)));
    add(opCodes, createSTA(STA_DirectPageIndexedWithX, createDirectIndexedWithXWriteCycles(Cpu65816::STA, M)));
    add(opCodes, createSTX(STX_DirectPageIndexedWithY, createDirectIndexedWithYWriteCycles(Cpu65816::STX, XY)));
    add(opCodes, createSTA(STA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYWriteCycles(Cpu65816::STA)));
    add(opCodes, createTYA(TYA_Implied, createImpliedCycles(Cpu65816::TYA)));
    add(opCodes, createSTA(STA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYWriteCycles(Cpu65816::STA)));
    add(opCodes, createTXS(TXS_Implied, createImpliedCycles(Cpu65816::TXS)));
    add(opCodes, createTXY(TXY_Implied, createImpliedCycles(Cpu65816::TXY)));
    add(opCodes, createSTZ(STZ_Absolute, createAbsoluteWriteCycles(Cpu65816::STZ, M)));
    add(opCodes, createSTA(STA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXWriteCycles(Cpu65816::STA)));
    add(opCodes, createSTZ(STZ_AbsoluteIndexedWithX, createAbsoluteIndexedWithXWriteCycles(Cpu65816::STZ)));
    add(opCodes, createSTA(STA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXWriteCycles(Cpu65816::STA)));
    add(opCodes, createLDY(LDY_Immediate, createImmediateCycles(Cpu65816::LDY, XY)));
    add(opCodes, createLDA(LDA_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::LDA)));
    add(opCodes, createLDX(LDX_Immediate, createImmediateCycles(Cpu65816::LDX, XY)));
    add(opCodes, createLDA(LDA_StackRelative, createStackRelativeCycles(Cpu65816::LDA)));
    add(opCodes, createLDY(LDY_DirectPage, createDirectCycles(Cpu65816::LDY, XY)));
    add(opCodes, createLDA(LDA_DirectPage, createDirectCycles(Cpu65816::LDA, M)));
    add(opCodes, createLDX(LDX_DirectPage, createDirectCycles(Cpu65816::LDX, XY)));
    add(opCodes, createLDA(LDA_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::LDA)));
    add(opCodes, createTAY(TAY_Implied, createImpliedCycles(Cpu65816::TAY)));
    add(opCodes, createLDA(LDA_Immediate, createImmediateCycles(Cpu65816::LDA, M)));
    add(opCodes, createTAX(TAX_Implied, createImpliedCycles(Cpu65816::TAX)));
    add(opCodes, createPLB(PLB_StackImplied, createStackPLBCycles(Cpu65816::PLB)));
    add(opCodes, createLDY(LDY_Absolute, createAbsoluteCycles(Cpu65816::LDY, XY)));
    add(opCodes, createLDA(LDA_Absolute, createAbsoluteCycles(Cpu65816::LDA, M)));
    add(opCodes, createLDX(LDX_Absolute, createAbsoluteCycles(Cpu65816::LDX, XY)));
    add(opCodes, createLDA(LDA_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::LDA)));
    add(opCodes, createBCS(BCS_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BCS)));
    add(opCodes, createLDA(LDA_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::LDA)));
    add(opCodes, createLDA(LDA_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::LDA)));
    add(opCodes, createLDA(LDA_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::LDA)));
    add(opCodes, createLDY(LDY_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::LDY, XY)));
    add(opCodes, createLDA(LDA_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::LDA, M)));
    add(opCodes, createLDX(LDX_DirectPageIndexedWithY, createDirectIndexedWithYCycles(Cpu65816::LDX, XY)));
    add(opCodes, createLDA(LDA_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::LDA)));
    add(opCodes, createCLV(CLV_Implied, createImpliedCycles(Cpu65816::CLV)));
    add(opCodes, createLDA(LDA_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::LDA, M)));
    add(opCodes, createTSX(TSX_Implied, createImpliedCycles(Cpu65816::TSX)));
    add(opCodes, createTYX(TYX_Implied, createImpliedCycles(Cpu65816::TYX)));
    add(opCodes, createLDY(LDY_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::LDY, XY)));
    add(opCodes, createLDA(LDA_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::LDA, M)));
    add(opCodes, createLDX(LDX_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::LDX, XY)));
    add(opCodes, createLDA(LDA_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::LDA)));
    add(opCodes, createCPY(CPY_Immediate, createImmediateCycles(Cpu65816::CPY, XY)));
    add(opCodes, createCMP(CMP_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::CMP)));
    add(opCodes, createREP(REP_Immediate, createImmediateREPSEPCycles(Cpu65816::REP)));
    add(opCodes, createCMP(CMP_StackRelative, createStackRelativeCycles(Cpu65816::CMP)));
    add(opCodes, createCPY(CPY_DirectPage, createDirectCycles(Cpu65816::CPY, XY)));
    add(opCodes, createCMP(CMP_DirectPage, createDirectCycles(Cpu65816::CMP, M)));
    add(opCodes, createDEC(DEC_DirectPage, createDirectRMWCycles(Cpu65816::DEC)));
    add(opCodes, createCMP(CMP_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::CMP)));
    add(opCodes, createINY(INY_Implied, createImpliedCycles(Cpu65816::INY)));
    add(opCodes, createCMP(CMP_Immediate, createImmediateCycles(Cpu65816::CMP, M)));
    add(opCodes, createDEX(DEX_Implied, createImpliedCycles(Cpu65816::DEX)));
    add(opCodes, createWAI(WAI_Implied, createWaitForInterruptCycles(Cpu65816::WAI)));
    add(opCodes, createCPY(CPY_Absolute, createAbsoluteCycles(Cpu65816::CPY, XY)));
    add(opCodes, createCMP(CMP_Absolute, createAbsoluteCycles(Cpu65816::CMP, M)));
    add(opCodes, createDEC(DEC_Absolute, createAbsoluteRMWCycles(Cpu65816::DEC)));
    add(opCodes, createCMP(CMP_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::CMP)));
    add(opCodes, createBNE(BNE_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BNE)));
    add(opCodes, createCMP(CMP_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::CMP)));
    add(opCodes, createCMP(CMP_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::CMP)));
    add(opCodes, createCMP(CMP_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::CMP)));
    add(opCodes, createPEI(PEI_StackDirectPageIndirect, createStackPEICycles()));
    add(opCodes, createCMP(CMP_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::CMP, M)));
    add(opCodes, createDEC(DEC_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::DEC)));
    add(opCodes, createCMP(CMP_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::CMP)));
    add(opCodes, createCLD(CLD_Implied, createImpliedCycles(Cpu65816::CLD)));
    add(opCodes, createCMP(CMP_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::CMP, M)));
    add(opCodes, createPHX(PHX_StackImplied, createStackPushCycles(Cpu65816::PHX, XY)));
    add(opCodes, createSTP(STP_Implied, createStopTheClockCycles(Cpu65816::STP)));
    add(opCodes, createJMP(JML_AbsoluteIndirectLong, createAbsoluteIndirectJMLCycles()));
    add(opCodes, createCMP(CMP_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::CMP, M)));
    add(opCodes, createDEC(DEC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::DEC)));
    add(opCodes, createCMP(CMP_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::CMP)));
    add(opCodes, createCPX(CPX_Immediate, createImmediateCycles(Cpu65816::CPX, XY)));
    add(opCodes, createSBC(SBC_DirectPageIndexedIndirectWithX, createDirectIndexedIndirectWithXCycles(Cpu65816::SBC)));
    add(opCodes, createSEP(SEP_Immediate, createImmediateREPSEPCycles(Cpu65816::SEP)));
    add(opCodes, createSBC(SBC_StackRelative, createStackRelativeCycles(Cpu65816::SBC)));
    add(opCodes, createCPX(CPX_DirectPage, createDirectCycles(Cpu65816::CPX, XY)));
    add(opCodes, createSBC(SBC_DirectPage, createDirectCycles(Cpu65816::SBC, M)));
    add(opCodes, createINC(INC_DirectPage, createDirectRMWCycles(Cpu65816::INC)));
    add(opCodes, createSBC(SBC_DirectPageIndirectLong, createDirectIndirectLongCycles(Cpu65816::SBC)));
    add(opCodes, createINX(INX_Implied, createImpliedCycles(Cpu65816::INX)));
    add(opCodes, createSBC(SBC_Immediate, createImmediateCycles(Cpu65816::SBC, M)));
    add(opCodes, createNOP(NOP_Implied, createImpliedCycles(Cpu65816::NOP)));
    add(opCodes, createXBA(XBA_Implied, createImpliedXBACycles(Cpu65816::XBA)));
    add(opCodes, createCPX(CPX_Absolute, createAbsoluteCycles(Cpu65816::CPX, XY)));
    add(opCodes, createSBC(SBC_Absolute, createAbsoluteCycles(Cpu65816::SBC, M)));
    add(opCodes, createINC(INC_Absolute, createAbsoluteRMWCycles(Cpu65816::INC)));
    add(opCodes, createSBC(SBC_AbsoluteLong, createAbsoluteLongCycles(Cpu65816::SBC)));
    add(opCodes, createBEQ(BEQ_ProgramCounterRelative, createRelativeShortCycles(Cpu65816::BEQ)));
    add(opCodes, createSBC(SBC_DirectPageIndirectIndexedWithY, createDirectIndirectIndexedWithYCycles(Cpu65816::SBC)));
    add(opCodes, createSBC(SBC_DirectPageIndirect, createDirectIndirectCycles(Cpu65816::SBC)));
    add(opCodes, createSBC(SBC_StackRelativeIndirectIndexedWithY, createStackRelativeIndirectIndexedWithYCycles(Cpu65816::SBC)));
    add(opCodes, createPEA(PEA_StackImmediate, createStackPEACycles()));
    add(opCodes, createSBC(SBC_DirectPageIndexedWithX, createDirectIndexedWithXCycles(Cpu65816::SBC, M)));
    add(opCodes, createINC(INC_DirectPageIndexedWithX, createDirectIndexedWithXRMWCycles(Cpu65816::INC)));
    add(opCodes, createSBC(SBC_DirectPageIndirectLongIndexedWithY, createDirectIndirectLongIndexedWithYCycles(Cpu65816::SBC)));
    add(opCodes, createSED(SED_Implied, createImpliedCycles(Cpu65816::SED)));
    add(opCodes, createSBC(SBC_AbsoluteIndexedWithY, createAbsoluteIndexedWithYCycles(Cpu65816::SBC, M)));
    add(opCodes, createPLX(PLX_StackImplied, createStackPullCycles(Cpu65816::PLX, XY)));
    add(opCodes, createXCE(XCE_Implied, createImpliedCycles(Cpu65816::XCE)));
    add(opCodes, createJSR(JSR_AbsoluteIndexedIndirectWithX, createAbsoluteIndexedIndirectWithXJSRCycles()));
    add(opCodes, createSBC(SBC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXCycles(Cpu65816::SBC, M)));
    add(opCodes, createINC(INC_AbsoluteIndexedWithX, createAbsoluteIndexedWithXRMWCycles(Cpu65816::INC)));
    add(opCodes, createSBC(SBC_AbsoluteLongIndexedWithX, createAbsoluteLongIndexedWithXCycles(Cpu65816::SBC)));

    for (int i = 0; i <= 255; i++)
    {
      OpCode opCode = opCodes.get(i);
      if (opCode.getCode() != i)
      {
        throw new EmulatorException("OpCode [" + opCode.getName() + "] has code [" + opCode.getCode() + "] but is at index [" + i + "].");
      }
    }

    return opCodes.toArray(new OpCode[0]);
  }

  private static OpCode createBEQ(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BEQ",
                      "Branch if Equal (Z=1)");
  }

  private static OpCode createXBA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "XBA",
                      "Exchange B and A Accumulator");
  }

  private static OpCode createNOP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "NOP",
                      "No Operation for two cycles.");
  }

  private static OpCode createINX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "INX",
                      "Increment Index X by One");
  }

  private static OpCode createSEP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "SEP",
                      "Set Processor Status Bit");
  }

  private static OpCode createBNE(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BNE",
                      "Branch if Not Equal (Z=0)");
  }

  private static OpCode createWAI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "WAI",
                      "Wait for Interrupt");
  }

  private static OpCode createDEX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "DEX",
                      "Decrement Index X by One.");
  }

  private static OpCode createINY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "INY",
                      "Increment Index Y by One");
  }

  private static OpCode createPEA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PEA",
                      "Push Absolute Address");
  }

  private static OpCode createINC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "INC",
                      "Increment memory; result in memory and update NZ.");
  }

  private static OpCode createXCE(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "XCE",
                      "Exchange Carry and Emulation Bits");
  }

  private static OpCode createPLX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLX",
                      "Pull Index X from Stack");
  }

  private static OpCode createSED(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles,
                      "SED", "Set Decimal Mode");
  }

  private static OpCode createSBC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "SBC",
                      "Subtract memory and carry from A; result in A and update NZC.");
  }

  private static OpCode createCPX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CPX",
                      "Compare Memory and Index X");
  }

  private static OpCode createSTP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "STP",
                      "Stop the Clock");
  }

  private static OpCode createPHX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHX",
                      "Push Index X on Stack");
  }

  private static OpCode createCLD(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CLD",
                      "Clear Decimal Mode");
  }

  private static OpCode createPEI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PEI",
                      "Push Indirect Address");
  }

  private static OpCode createDEC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "DEC",
                      "Decrement memory; result in memory and update NZ.");
  }

  private static OpCode createREP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "REP",
                      "Reset Status Bits");
  }

  private static OpCode createCMP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CMP",
                      "Compare Memory and Accumulator");
  }

  private static OpCode createCPY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CPY",
                      "Compare Memory and Index Y");
  }

  private static OpCode createTYX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TYX",
                      "Transfer Index Y to Index X");
  }

  private static OpCode createTSX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TSX",
                      "Transfer Stack Pointer Register to Index X");
  }

  private static OpCode createCLV(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CLV",
                      "Clear Overflow Flag");
  }

  private static OpCode createBCS(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BCS",
                      "Branch on Carry Set (C=1)");
  }

  private static OpCode createPLB(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLB",
                      "Pull Data Bank Register from Stack");
  }

  private static OpCode createTAX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TAX",
                      "Transfer Accumulator in Index X");
  }

  private static OpCode createTAY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TAY",
                      "Transfer Accumulator in Index Y");
  }

  private static OpCode createLDX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "LDX", "Load Index X with Memory");
  }

  private static OpCode createBRL(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BRL",
                      "Branch Always Long");
  }

  private static OpCode createBRA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BRA",
                      "Branch Always");
  }

  private static OpCode createTDC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TDC",
                      "Transfer Direct Register to C Accumulator");
  }

  private static OpCode createPLY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLY",
                      "Pull Index Y from Stack");
  }

  private static OpCode createSEI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "SEI",
                      "Set Interrupt Disable Status");
  }

  private static OpCode createBVS(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BVS", "Branch on Overflow Set (V=1)");
  }

  private static OpCode createRTL(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "RTL",
                      "Return from Subroutine Long");
  }

  private static OpCode createRRA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ROR",
                      "Rotate accumulator right one bit; update NZC.");
  }

  private static OpCode createPLA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLA",
                      "Pull Accumulator from Stack");
  }

  private static OpCode createROR(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ROR",
                      "Rotate memory right one bit; update NZC.");
  }

  private static OpCode createPER(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PER",
                      "Push Program Counter Relative Address");
  }

  private static OpCode createADC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ADC",
                      "Add memory and carry to A; result in A and update NZC.");
  }

  private static OpCode createRTS(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "RTS",
                      "Return from Subroutine");
  }

  private static OpCode createTCD(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TCD",
                      "Transfer C Accumulator to Direct Register");
  }

  private static OpCode createPHY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHY",
                      "Push Index Y on Stack");
  }

  private static OpCode createCLI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CLI",
                      "Clear Interrupt Disable Bit");
  }

  private static OpCode createMVN(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "MVN",
                      "Block move next...");
  }

  private static OpCode createBVC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BVC",
                      "Branch on Overflow Clear (V=0)");
  }

  private static OpCode createJMP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "JMP",
                      "Jump to New Location");
  }

  private static OpCode createPHK(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHK",
                      "Push Program Bank Register on Stack");
  }

  private static OpCode createSRA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "LSR",
                      "Shift accumulator right one bit; update NZC.");
  }

  private static OpCode createPHA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHA",
                      "Push Accumulator onto Stack");
  }

  private static OpCode createLSR(int code_lsr, InstructionCycles instructionCycles)
  {
    return new OpCode(code_lsr, instructionCycles,
                      "LSR", "Shift memory right one bit; update NZC.");
  }

  private static OpCode createMVP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "MVP",
                      "Block move previous...");
  }

  private static OpCode createWDM(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "WDM",
                      "Reserved for future use");
  }

  private static OpCode createEOR(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "EOR",
                      "'Exclusive OR' Memory with Accumulator");
  }

  private static OpCode createRTI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "RTI",
                      "Return from Interrupt");
  }

  private static OpCode createTSC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TSC",
                      "Transfer Stack Pointer to C Accumulator");
  }

  private static OpCode createDEA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "DEC",
                      "Decrement accumulator; update NZ.");
  }

  private static OpCode createSEC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "SEC",
                      "Set Carry Flag");
  }

  private static OpCode createBMI(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BMI",
                      "Branch if Result Minus (N=1)");
  }

  private static OpCode createPLP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLP",
                      "Pull Processor Status from Stack");
  }

  private static OpCode createRLA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ROL",
                      "Rotate Accumulator One Bit Left.");
  }

  private static OpCode createPLD(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PLD",
                      "Pull Direct Register from Stack");
  }

  private static OpCode createROL(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ROL",
                      "Rotate Memory One Bit Left.");
  }

  private static OpCode createLDA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "LDA",
                      "Load Accumulator with Memory");
  }

  private static OpCode createLDY(int code, InstructionCycles immediateCycles)
  {
    return new OpCode(code, immediateCycles, "LDY",
                      "Load Index Y with Memory");
  }

  private static OpCode createPHB(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHB",
                      "Push Data Bank Register on Stack");
  }

  private static OpCode createTXA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TXA",
                      "Transfer Index X to Accumulator");
  }

  private static OpCode createSTY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "STY",
                      "Store Index Y in Memory");
  }

  private static OpCode createBCC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BCC",
                      "Branch on Carry Clear (C=0)");
  }

  private static OpCode createSTX(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "STX",
                      "Store Index X in Memory");
  }

  private static OpCode createTYA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TYA",
                      "Transfer Index Y to Accumulator");
  }

  private static OpCode createTXS(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TXS",
                      "Transfer Index X to Stack Pointer Register");
  }

  private static OpCode createTXY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TXY",
                      "Transfer Index X to Index Y");
  }

  private static OpCode createSTZ(int stz_absolute, InstructionCycles absoluteWriteCycles)
  {
    return new OpCode(stz_absolute, absoluteWriteCycles,
                      "STZ", "Store Zero in Memory");
  }

  private static OpCode createSTA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "STA",
                      "Store Accumulator in Memory");
  }

  private static OpCode createDEY(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "DEY", "Decrement Index Y by One");
  }

  private static OpCode createBIT(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BIT",
                      "Bit Test");
  }

  private static OpCode createJSL(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "JSL",
                      "Jump long to new location save return address on Stack.");
  }

  private static OpCode createAND(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "AND",
                      "Bitwise AND memory with A; result in A and update NZ.");
  }

  private static OpCode createJSR(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "JSR",
                      "Jump to new location save return address on Stack.");
  }

  private static OpCode createTCS(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TCS",
                      "Transfer C Accumulator to Stack Pointer");
  }

  private static OpCode createINA(int code, InstructionCycles busCycles)
  {
    return new OpCode(code, busCycles, "INC",
                      "Increment accumulator; update NZ.");
  }

  private static OpCode createCLC(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "CLC",
                      "Clear Carry Flag");
  }

  private static OpCode createTRB(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "TRB",
                      "Test and Reset Bit");
  }

  private static OpCode createBPL(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BPL",
                      "Branch if Result Plus (N=0)");
  }

  private static OpCode createPHD(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHD",
                      "Push Direct Register on Stack");
  }

  private static OpCode createSLA(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "ASL",
                      "Shift accumulator left one bit; update NZC.");
  }

  private static OpCode createASL(int code, InstructionCycles directRMWCycles)
  {
    return new OpCode(code, directRMWCycles, "ASL", "Shift memory left 1 bit; result in memory and update NZC.");
  }

  private static OpCode createPHP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "PHP", "Push Processor Status on Stack");
  }

  private static OpCode createTSB(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles,
                      "TSB", "Test and Set Bit");
  }

  private static OpCode createCOP(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "COP",
                      "Force co-processor software interrupt.");
  }

  private static OpCode createORA(int code, InstructionCycles directIndexedIndirectWithXCycles)
  {
    return new OpCode(code, directIndexedIndirectWithXCycles,
                      "ORA", "'OR' memory with A; result in A and update NZ.");
  }

  private static OpCode createBrk(int code, InstructionCycles instructionCycles)
  {
    return new OpCode(code, instructionCycles, "BRK",
                      "Force break software interrupt.");
  }

  public static OpCode createReset()
  {
    return new OpCode(-1, createStackResetCycles(new ResetVector(), Cpu65816::RES), "RES",
                      "Reset the CPU.");
  }

  public static OpCode createIRQ()
  {
    return new OpCode(-1, createStackHardwareInterruptCycles(new IRQVector(), Cpu65816::IRQ), "IRQ",
                      "Interrupt request.");
  }

  public static OpCode createNMI()
  {
    return new OpCode(-1, createStackHardwareInterruptCycles(new NMIVector(), Cpu65816::NMI), "NMI",
                      "Non-maskable interrupt.");
  }

  public static OpCode createAbort()
  {
    return new OpCode(-1, createStackHardwareInterruptCycles(new AbortVector(), Cpu65816::ABORT), "ABORT",
                      "Stop the current instruction and return processor status to what it was prior to the current instruction.");
  }

  public static OpCode createFetchNext()
  {
    return new OpCode(-1, createFetchOpCodeCycles(), "Fetch Opcode",
                      "Fetch Opcode");
  }

  private static void add(List<OpCode> opCodes, OpCode opCode)
  {
    opCodes.add(opCode);
  }
}

