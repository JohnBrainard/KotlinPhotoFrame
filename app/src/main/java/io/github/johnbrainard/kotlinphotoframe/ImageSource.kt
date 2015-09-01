package io.github.johnbrainard.kotlinphotoframe

class ImageSource(images: Array<String>) {
	val images: Array<String> = images
	var index = 0

	fun nextImage(): String {
		val image = images.get(index++)

		if (index >= images.size())
			index = 0

		return image
	}
}
