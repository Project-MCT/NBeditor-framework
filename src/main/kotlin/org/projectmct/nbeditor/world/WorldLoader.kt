package org.projectmct.nbeditor.world

import java.io.File

interface WorldLoader {
  fun loadWorld(file: File): World

}