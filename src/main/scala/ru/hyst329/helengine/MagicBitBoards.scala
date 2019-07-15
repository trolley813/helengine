package ru.hyst329.helengine

import java.nio.{ByteBuffer, ByteOrder}

import ru.hyst329.helengine.Global._

import scala.annotation.tailrec
import scala.io.Source

object MagicBitBoards {
  val KingPatterns: Array[BitBoard] = Array(
    0x0000000000000302l, 0x0000000000000705l, 0x0000000000000e0al, 0x0000000000001c14l,
    0x0000000000003828l, 0x0000000000007050l, 0x000000000000e0a0l, 0x000000000000c040l,
    0x0000000000030203l, 0x0000000000070507l, 0x00000000000e0a0el, 0x00000000001c141cl,
    0x0000000000382838l, 0x0000000000705070l, 0x0000000000e0a0e0l, 0x0000000000c040c0l,
    0x0000000003020300l, 0x0000000007050700l, 0x000000000e0a0e00l, 0x000000001c141c00l,
    0x0000000038283800l, 0x0000000070507000l, 0x00000000e0a0e000l, 0x00000000c040c000l,
    0x0000000302030000l, 0x0000000705070000l, 0x0000000e0a0e0000l, 0x0000001c141c0000l,
    0x0000003828380000l, 0x0000007050700000l, 0x000000e0a0e00000l, 0x000000c040c00000l,
    0x0000030203000000l, 0x0000070507000000l, 0x00000e0a0e000000l, 0x00001c141c000000l,
    0x0000382838000000l, 0x0000705070000000l, 0x0000e0a0e0000000l, 0x0000c040c0000000l,
    0x0003020300000000l, 0x0007050700000000l, 0x000e0a0e00000000l, 0x001c141c00000000l,
    0x0038283800000000l, 0x0070507000000000l, 0x00e0a0e000000000l, 0x00c040c000000000l,
    0x0302030000000000l, 0x0705070000000000l, 0x0e0a0e0000000000l, 0x1c141c0000000000l,
    0x3828380000000000l, 0x7050700000000000l, 0xe0a0e00000000000l, 0xc040c00000000000l,
    0x0203000000000000l, 0x0507000000000000l, 0x0a0e000000000000l, 0x141c000000000000l,
    0x2838000000000000l, 0x5070000000000000l, 0xa0e0000000000000l, 0x40c0000000000000l
  )

  val KnightPatterns: Array[BitBoard] = Array(
    0x0000000000020400l, 0x0000000000050800l, 0x00000000000a1100l, 0x0000000000142200l,
    0x0000000000284400l, 0x0000000000508800l, 0x0000000000a01000l, 0x0000000000402000l,
    0x0000000002040004l, 0x0000000005080008l, 0x000000000a110011l, 0x0000000014220022l,
    0x0000000028440044l, 0x0000000050880088l, 0x00000000a0100010l, 0x0000000040200020l,
    0x0000000204000402l, 0x0000000508000805l, 0x0000000a1100110al, 0x0000001422002214l,
    0x0000002844004428l, 0x0000005088008850l, 0x000000a0100010a0l, 0x0000004020002040l,
    0x0000020400040200l, 0x0000050800080500l, 0x00000a1100110a00l, 0x0000142200221400l,
    0x0000284400442800l, 0x0000508800885000l, 0x0000a0100010a000l, 0x0000402000204000l,
    0x0002040004020000l, 0x0005080008050000l, 0x000a1100110a0000l, 0x0014220022140000l,
    0x0028440044280000l, 0x0050880088500000l, 0x00a0100010a00000l, 0x0040200020400000l,
    0x0204000402000000l, 0x0508000805000000l, 0x0a1100110a000000l, 0x1422002214000000l,
    0x2844004428000000l, 0x5088008850000000l, 0xa0100010a0000000l, 0x4020002040000000l,
    0x0400040200000000l, 0x0800080500000000l, 0x1100110a00000000l, 0x2200221400000000l,
    0x4400442800000000l, 0x8800885000000000l, 0x100010a000000000l, 0x2000204000000000l,
    0x0004020000000000l, 0x0008050000000000l, 0x00110a0000000000l, 0x0022140000000000l,
    0x0044280000000000l, 0x0088500000000000l, 0x0010a00000000000l, 0x0020400000000000l
  )

  val WhitePawnMovePatterns: Array[BitBoard] = Array(
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000001010000l, 0x0000000002020000l, 0x0000000004040000l, 0x0000000008080000l,
    0x0000000010100000l, 0x0000000020200000l, 0x0000000040400000l, 0x0000000080800000l,
    0x0000000001000000l, 0x0000000002000000l, 0x0000000004000000l, 0x0000000008000000l,
    0x0000000010000000l, 0x0000000020000000l, 0x0000000040000000l, 0x0000000080000000l,
    0x0000000100000000l, 0x0000000200000000l, 0x0000000400000000l, 0x0000000800000000l,
    0x0000001000000000l, 0x0000002000000000l, 0x0000004000000000l, 0x0000008000000000l,
    0x0000010000000000l, 0x0000020000000000l, 0x0000040000000000l, 0x0000080000000000l,
    0x0000100000000000l, 0x0000200000000000l, 0x0000400000000000l, 0x0000800000000000l,
    0x0001000000000000l, 0x0002000000000000l, 0x0004000000000000l, 0x0008000000000000l,
    0x0010000000000000l, 0x0020000000000000l, 0x0040000000000000l, 0x0080000000000000l,
    0x0100000000000000l, 0x0200000000000000l, 0x0400000000000000l, 0x0800000000000000l,
    0x1000000000000000l, 0x2000000000000000l, 0x4000000000000000l, 0x8000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l
  )

  val BlackPawnMovePatterns: Array[BitBoard] = Array(
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000001l, 0x0000000000000002l, 0x0000000000000004l, 0x0000000000000008l,
    0x0000000000000010l, 0x0000000000000020l, 0x0000000000000040l, 0x0000000000000080l,
    0x0000000000000100l, 0x0000000000000200l, 0x0000000000000400l, 0x0000000000000800l,
    0x0000000000001000l, 0x0000000000002000l, 0x0000000000004000l, 0x0000000000008000l,
    0x0000000000010000l, 0x0000000000020000l, 0x0000000000040000l, 0x0000000000080000l,
    0x0000000000100000l, 0x0000000000200000l, 0x0000000000400000l, 0x0000000000800000l,
    0x0000000001000000l, 0x0000000002000000l, 0x0000000004000000l, 0x0000000008000000l,
    0x0000000010000000l, 0x0000000020000000l, 0x0000000040000000l, 0x0000000080000000l,
    0x0000000100000000l, 0x0000000200000000l, 0x0000000400000000l, 0x0000000800000000l,
    0x0000001000000000l, 0x0000002000000000l, 0x0000004000000000l, 0x0000008000000000l,
    0x0000010100000000l, 0x0000020200000000l, 0x0000040400000000l, 0x0000080800000000l,
    0x0000101000000000l, 0x0000202000000000l, 0x0000404000000000l, 0x0000808000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l
  )

  val WhitePawnCapturePatterns: Array[BitBoard] = Array(
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000020000l, 0x0000000000040000l, 0x0000000000080000l, 0x0000000000100000l,
    0x0000000000200000l, 0x0000000000400000l, 0x0000000000800000l, 0x0000000000000000l,
    0x0000000002000000l, 0x0000000004000000l, 0x0000000008000000l, 0x0000000010000000l,
    0x0000000020000000l, 0x0000000040000000l, 0x0000000080000000l, 0x0000000000000000l,
    0x0000000200000000l, 0x0000000400000000l, 0x0000000800000000l, 0x0000001000000000l,
    0x0000002000000000l, 0x0000004000000000l, 0x0000008000000000l, 0x0000000000000000l,
    0x0000020000000000l, 0x0000040000000000l, 0x0000080000000000l, 0x0000100000000000l,
    0x0000200000000000l, 0x0000400000000000l, 0x0000800000000000l, 0x0000000000000000l,
    0x0002000000000000l, 0x0004000000000000l, 0x0008000000000000l, 0x0010000000000000l,
    0x0020000000000000l, 0x0040000000000000l, 0x0080000000000000l, 0x0000000000000000l,
    0x0200000000000000l, 0x0400000000000000l, 0x0800000000000000l, 0x1000000000000000l,
    0x2000000000000000l, 0x4000000000000000l, 0x8000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l
  )

  val BlackPawnCapturePatterns: Array[BitBoard] = Array(
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000002l, 0x0000000000000004l, 0x0000000000000008l, 0x0000000000000010l,
    0x0000000000000020l, 0x0000000000000040l, 0x0000000000000080l, 0x0000000000000000l,
    0x0000000000000200l, 0x0000000000000400l, 0x0000000000000800l, 0x0000000000001000l,
    0x0000000000002000l, 0x0000000000004000l, 0x0000000000008000l, 0x0000000000000000l,
    0x0000000000020000l, 0x0000000000040000l, 0x0000000000080000l, 0x0000000000100000l,
    0x0000000000200000l, 0x0000000000400000l, 0x0000000000800000l, 0x0000000000000000l,
    0x0000000002000000l, 0x0000000004000000l, 0x0000000008000000l, 0x0000000010000000l,
    0x0000000020000000l, 0x0000000040000000l, 0x0000000080000000l, 0x0000000000000000l,
    0x0000000200000000l, 0x0000000400000000l, 0x0000000800000000l, 0x0000001000000000l,
    0x0000002000000000l, 0x0000004000000000l, 0x0000008000000000l, 0x0000000000000000l,
    0x0000020000000000l, 0x0000040000000000l, 0x0000080000000000l, 0x0000100000000000l,
    0x0000200000000000l, 0x0000400000000000l, 0x0000800000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l,
    0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l, 0x0000000000000000l
  )

  val RookMasks: Array[BitBoard] = Array(
    0x000101010101017el, 0x000202020202027cl, 0x000404040404047al, 0x0008080808080876l,
    0x001010101010106el, 0x002020202020205el, 0x004040404040403el, 0x008080808080807el,
    0x0001010101017e00l, 0x0002020202027c00l, 0x0004040404047a00l, 0x0008080808087600l,
    0x0010101010106e00l, 0x0020202020205e00l, 0x0040404040403e00l, 0x0080808080807e00l,
    0x00010101017e0100l, 0x00020202027c0200l, 0x00040404047a0400l, 0x0008080808760800l,
    0x00101010106e1000l, 0x00202020205e2000l, 0x00404040403e4000l, 0x00808080807e8000l,
    0x000101017e010100l, 0x000202027c020200l, 0x000404047a040400l, 0x0008080876080800l,
    0x001010106e101000l, 0x002020205e202000l, 0x004040403e404000l, 0x008080807e808000l,
    0x0001017e01010100l, 0x0002027c02020200l, 0x0004047a04040400l, 0x0008087608080800l,
    0x0010106e10101000l, 0x0020205e20202000l, 0x0040403e40404000l, 0x0080807e80808000l,
    0x00017e0101010100l, 0x00027c0202020200l, 0x00047a0404040400l, 0x0008760808080800l,
    0x00106e1010101000l, 0x00205e2020202000l, 0x00403e4040404000l, 0x00807e8080808000l,
    0x007e010101010100l, 0x007c020202020200l, 0x007a040404040400l, 0x0076080808080800l,
    0x006e101010101000l, 0x005e202020202000l, 0x003e404040404000l, 0x007e808080808000l,
    0x7e01010101010100l, 0x7c02020202020200l, 0x7a04040404040400l, 0x7608080808080800l,
    0x6e10101010101000l, 0x5e20202020202000l, 0x3e40404040404000l, 0x7e80808080808000l
  )

  val BishopMasks: Array[BitBoard] = Array(
    0x0040201008040200l, 0x0000402010080400l, 0x0000004020100a00l, 0x0000000040221400l,
    0x0000000002442800l, 0x0000000204085000l, 0x0000020408102000l, 0x0002040810204000l,
    0x0020100804020000l, 0x0040201008040000l, 0x00004020100a0000l, 0x0000004022140000l,
    0x0000000244280000l, 0x0000020408500000l, 0x0002040810200000l, 0x0004081020400000l,
    0x0010080402000200l, 0x0020100804000400l, 0x004020100a000a00l, 0x0000402214001400l,
    0x0000024428002800l, 0x0002040850005000l, 0x0004081020002000l, 0x0008102040004000l,
    0x0008040200020400l, 0x0010080400040800l, 0x0020100a000a1000l, 0x0040221400142200l,
    0x0002442800284400l, 0x0004085000500800l, 0x0008102000201000l, 0x0010204000402000l,
    0x0004020002040800l, 0x0008040004081000l, 0x00100a000a102000l, 0x0022140014224000l,
    0x0044280028440200l, 0x0008500050080400l, 0x0010200020100800l, 0x0020400040201000l,
    0x0002000204081000l, 0x0004000408102000l, 0x000a000a10204000l, 0x0014001422400000l,
    0x0028002844020000l, 0x0050005008040200l, 0x0020002010080400l, 0x0040004020100800l,
    0x0000020408102000l, 0x0000040810204000l, 0x00000a1020400000l, 0x0000142240000000l,
    0x0000284402000000l, 0x0000500804020000l, 0x0000201008040200l, 0x0000402010080400l,
    0x0002040810204000l, 0x0004081020400000l, 0x000a102040000000l, 0x0014224000000000l,
    0x0028440200000000l, 0x0050080402000000l, 0x0020100804020000l, 0x0040201008040200l
  )

  val RookMagic: Array[BitBoard] = Array(
    0x0a8002c000108020l, 0x06c00049b0002001l, 0x0100200010090040l, 0x2480041000800801l,
    0x0280028004000800l, 0x0900410008040022l, 0x0280020001001080l, 0x2880002041000080l,
    0xa000800080400034l, 0x0004808020004000l, 0x2290802004801000l, 0x0411000d00100020l,
    0x0402800800040080l, 0x000b000401004208l, 0x2409000100040200l, 0x0001002100004082l,
    0x0022878001e24000l, 0x1090810021004010l, 0x0801030040200012l, 0x0500808008001000l,
    0x0a08018014000880l, 0x8000808004000200l, 0x0201008080010200l, 0x0801020000441091l,
    0x0000800080204005l, 0x1040200040100048l, 0x0000120200402082l, 0x0d14880480100080l,
    0x0012040280080080l, 0x0100040080020080l, 0x9020010080800200l, 0x0813241200148449l,
    0x0491604001800080l, 0x0100401000402001l, 0x4820010021001040l, 0x0400402202000812l,
    0x0209009005000802l, 0x0810800601800400l, 0x4301083214000150l, 0x204026458e001401l,
    0x0040204000808000l, 0x8001008040010020l, 0x8410820820420010l, 0x1003001000090020l,
    0x0804040008008080l, 0x0012000810020004l, 0x1000100200040208l, 0x430000a044020001l,
    0x0280009023410300l, 0x00e0100040002240l, 0x0000200100401700l, 0x2244100408008080l,
    0x0008000400801980l, 0x0002000810040200l, 0x8010100228810400l, 0x2000009044210200l,
    0x4080008040102101l, 0x0040002080411d01l, 0x2005524060000901l, 0x0502001008400422l,
    0x489a000810200402l, 0x0001004400080a13l, 0x4000011008020084l, 0x0026002114058042l
  )

  val BishopMagic: Array[BitBoard] = Array(
    0x89a1121896040240l, 0x2004844802002010l, 0x2068080051921000l, 0x62880a0220200808l,
    0x0004042004000000l, 0x0100822020200011l, 0xc00444222012000al, 0x0028808801216001l,
    0x0400492088408100l, 0x0201c401040c0084l, 0x00840800910a0010l, 0x0000082080240060l,
    0x2000840504006000l, 0x30010c4108405004l, 0x1008005410080802l, 0x8144042209100900l,
    0x0208081020014400l, 0x004800201208ca00l, 0x0f18140408012008l, 0x1004002802102001l,
    0x0841000820080811l, 0x0040200200a42008l, 0x0000800054042000l, 0x88010400410c9000l,
    0x0520040470104290l, 0x1004040051500081l, 0x2002081833080021l, 0x000400c00c010142l,
    0x941408200c002000l, 0x0658810000806011l, 0x0188071040440a00l, 0x4800404002011c00l,
    0x0104442040404200l, 0x0511080202091021l, 0x0004022401120400l, 0x80c0040400080120l,
    0x8040010040820802l, 0x0480810700020090l, 0x0102008e00040242l, 0x0809005202050100l,
    0x8002024220104080l, 0x0431008804142000l, 0x0019001802081400l, 0x0200014208040080l,
    0x3308082008200100l, 0x041010500040c020l, 0x4012020c04210308l, 0x208220a202004080l,
    0x0111040120082000l, 0x6803040141280a00l, 0x2101004202410000l, 0x8200000041108022l,
    0x0000021082088000l, 0x0002410204010040l, 0x0040100400809000l, 0x0822088220820214l,
    0x0040808090012004l, 0x00910224040218c9l, 0x0402814422015008l, 0x0090014004842410l,
    0x0001000042304105l, 0x0010008830412a00l, 0x2520081090008908l, 0x40102000a0a60140l
  )

  val RookBits = Array(12, 11, 11, 11, 11, 11, 11, 12, 11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10,
    10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10,
    10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 12, 11, 11, 11, 11, 11, 11, 12)

  val BishopBits = Array(6, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 7, 7, 7, 5, 5, 5,
    5, 7, 9, 9, 7, 5, 5, 5, 5, 7, 9, 9, 7, 5, 5, 5, 5, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6,
    5, 5, 5, 5, 5, 5, 6)

  val RookAttackTable: Array[Array[BitBoard]]   = Array.ofDim(64, 4096)
  val BishopAttackTable: Array[Array[BitBoard]] = Array.ofDim(64, 512)

  {
    // Initialisation block for rook/bishop attacks
    // TODO: Load from resources
    val rookStream   = getClass.getResourceAsStream("/RookAttackTable.dat")
    val bishopStream = getClass.getResourceAsStream("/BishopAttackTable.dat")
    (0 to 63).foreach { square =>
      val rookBytes: Array[Byte]   = Array.ofDim(4096 * 8)
      val bishopBytes: Array[Byte] = Array.ofDim(512 * 8)
      rookStream.read(rookBytes, 0, rookBytes.length)
      bishopStream.read(bishopBytes, 0, bishopBytes.length)
      ByteBuffer
        .allocate(rookBytes.length)
        .put(rookBytes)
        .flip()
        .asInstanceOf[ByteBuffer]
        .order(ByteOrder.LITTLE_ENDIAN)
        .asLongBuffer()
        .get(RookAttackTable(square))
      ByteBuffer
        .allocate(bishopBytes.length)
        .put(bishopBytes)
        .flip()
        .asInstanceOf[ByteBuffer]
        .order(ByteOrder.LITTLE_ENDIAN)
        .asLongBuffer()
        .get(BishopAttackTable(square))
    }
  }
}
