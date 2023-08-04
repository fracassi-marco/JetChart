package io.jetchart.pie

data class Pies(val slices: List<Slice>) {
  fun totalSize(): Float = slices.map { it.value }.sum()
}