import tkinter
from tkinter import filedialog

import numpy as np
from PIL import Image, ImageTk
from matplotlib import pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

cnt = 0
created_image = ''
image = None
brightness_timer_id = None
contrast_timer_id = None


def open_image(root, file_path):
    global cnt
    global image
    cnt += 1
    canvas = tkinter.Canvas(root, height=320, width=480)
    image = Image.open(file_path)
    photo = ImageTk.PhotoImage(image)
    canvas.create_image(0, 0, anchor='nw', image=photo)
    canvas.grid(row=1, column=2, rowspan=7)

    image = Image.open(file_path).convert("RGB")
    root.mainloop()


def create_gray_image(root):
    global image
    global created_image
    new_image_canvas = tkinter.Canvas(root, width=480, height=320)
    new_image_canvas.grid(row=1, column=3, rowspan=7)

    new_image = Image.new(size=(480, 320), mode="RGB")
    for i in range(image.width):
        for j in range(image.height):
            r, g, b = image.getpixel((i, j))
            color = (r + g + b) // 3
            new_image.putpixel((i, j), (color, color, color))

    new_photo = ImageTk.PhotoImage(new_image)
    new_image_canvas.image = new_photo
    new_image_canvas.create_image(0, 0, anchor='nw', image=new_photo)
    created_image = new_image
    create_histograms_with_image(root, new_image)
    root.mainloop()


def create_inverted_image(root):
    global image
    global created_image
    new_image_canvas = tkinter.Canvas(root, width=480, height=320)
    new_image_canvas.grid(row=1, column=3, rowspan=7)

    new_image = Image.new(size=(480, 320), mode="RGB")
    for i in range(image.width):
        for j in range(image.height):
            r, g, b = image.getpixel((i, j))
            new_image.putpixel((i, j), (255 - r, 255 - g, 255 - b))

    new_photo = ImageTk.PhotoImage(new_image)
    new_image_canvas.image = new_photo
    new_image_canvas.create_image(0, 0, anchor='nw', image=new_photo)
    created_image = new_image
    create_histograms_with_image(root, new_image)
    root.mainloop()


def create_blured_image(root):
    global image
    global created_image
    n = 11
    new_image_canvas = tkinter.Canvas(root, width=480, height=320)
    new_image_canvas.grid(row=1, column=3, rowspan=7)

    temp_image = Image.new(size=(480, 320), mode="RGB")

    for j in range(image.height):
        for i in range(image.width):
            sum_r, sum_g, sum_b, count = 0, 0, 0, 0

            for k in range(max(0, i - n // 2), min(image.width, i + n // 2 + 1)):
                r, g, b = image.getpixel((k, j))
                sum_r += r
                sum_g += g
                sum_b += b
                count += 1
            temp_image.putpixel((i, j), (sum_r // count, sum_g // count, sum_b // count))

    new_image = Image.new(size=(480, 320), mode="RGB")
    for i in range(image.width):
        for j in range(image.height):
            sum_r, sum_g, sum_b, count = 0, 0, 0, 0

            for m in range(max(0, j - n // 2), min(image.height, j + n // 2 + 1)):
                r, g, b = temp_image.getpixel((i, m))
                sum_r += r
                sum_g += g
                sum_b += b
                count += 1
            new_image.putpixel((i, j), (sum_r // count, sum_g // count, sum_b // count))

    new_photo = ImageTk.PhotoImage(new_image)
    new_image_canvas.image = new_photo
    new_image_canvas.create_image(0, 0, anchor='nw', image=new_photo)
    created_image = new_image
    create_histograms_with_image(root, new_image)
    root.mainloop()


def create_brightness_image(root, brightness_factor):
    global image
    global created_image

    new_image_canvas = tkinter.Canvas(root, width=480, height=320)
    new_image_canvas.grid(row=1, column=3, rowspan=7)

    new_image = Image.new(size=(480, 320), mode="RGB")

    for i in range(image.width):
        for j in range(image.height):
            r, g, b = image.getpixel((i, j))

            new_r = int(r * brightness_factor)
            new_g = int(g * brightness_factor)
            new_b = int(b * brightness_factor)

            new_r = max(0, min(255, new_r))
            new_g = max(0, min(255, new_g))
            new_b = max(0, min(255, new_b))

            new_image.putpixel((i, j), (new_r, new_g, new_b))

    new_photo = ImageTk.PhotoImage(new_image)
    new_image_canvas.image = new_photo
    new_image_canvas.create_image(0, 0, anchor='nw', image=new_photo)
    created_image = new_image
    create_histograms_with_image(root, new_image)
    root.mainloop()


def on_brightness_slider_change(value, root):
    global brightness_timer_id

    if brightness_timer_id is not None:
        root.after_cancel(brightness_timer_id)

    brightness_timer_id = root.after(1000, apply_brightness, root, float(value))


def apply_brightness(root, brightness_factor):
    global brightness_timer_id
    brightness_timer_id = None
    create_brightness_image(root, brightness_factor)


def create_contrast_image(root, contrast_factor):
    global image
    global created_image

    new_image_canvas = tkinter.Canvas(root, width=480, height=320)
    new_image_canvas.grid(row=1, column=3, rowspan=7)

    new_image = Image.new(size=(480, 320), mode="RGB")

    for i in range(image.width):
        for j in range(image.height):
            r, g, b = image.getpixel((i, j))

            new_r = int(contrast_factor * (r - 128) + 128)
            new_g = int(contrast_factor * (g - 128) + 128)
            new_b = int(contrast_factor * (b - 128) + 128)

            new_r = max(0, min(255, new_r))
            new_g = max(0, min(255, new_g))
            new_b = max(0, min(255, new_b))

            new_image.putpixel((i, j), (new_r, new_g, new_b))

    new_photo = ImageTk.PhotoImage(new_image)
    new_image_canvas.image = new_photo
    new_image_canvas.create_image(0, 0, anchor='nw', image=new_photo)
    created_image = new_image
    create_histograms_with_image(root, new_image)
    root.mainloop()


def on_contrast_slider_change(value, root):
    global contrast_timer_id

    if contrast_timer_id is not None:
        root.after_cancel(contrast_timer_id)

    contrast_timer_id = root.after(1000, apply_contrast, root, float(value))


def apply_contrast(root, contrast_factor):
    global contrast_timer_id
    contrast_timer_id = None
    create_contrast_image(root, contrast_factor)


def create_histograms(root):
    global image

    hist_frame = tkinter.Frame(root)
    hist_frame.grid(row=8, column=0, columnspan=4, pady=10, padx=10, sticky='we')

    fig, axes = plt.subplots(1, 3, figsize=(12, 3))
    fig.suptitle('Гистограммы насыщенности по каналам')

    img_array = np.array(image)

    axes[0].hist(img_array[:, :, 0].flatten(), bins=50, color='red', alpha=0.7, range=(0, 255))
    axes[0].set_title('Красный канал')
    axes[0].set_xlabel('Насыщенность')
    axes[0].set_ylabel('Частота')
    axes[0].grid(True, alpha=0.3)

    axes[1].hist(img_array[:, :, 1].flatten(), bins=50, color='green', alpha=0.7, range=(0, 255))
    axes[1].set_title('Зеленый канал')
    axes[1].set_xlabel('Насыщенность')
    axes[1].set_ylabel('Частота')
    axes[1].grid(True, alpha=0.3)

    axes[2].hist(img_array[:, :, 2].flatten(), bins=50, color='blue', alpha=0.7, range=(0, 255))
    axes[2].set_title('Синий канал')
    axes[2].set_xlabel('Насыщенность')
    axes[2].set_ylabel('Частота')
    axes[2].grid(True, alpha=0.3)

    plt.tight_layout()

    canvas = FigureCanvasTkAgg(fig, master=hist_frame)
    canvas.draw()
    canvas.get_tk_widget().pack(fill=tkinter.BOTH, expand=True)

    return canvas


def create_histograms_with_image(root, img):
    hist_frame = tkinter.Frame(root)
    hist_frame.grid(row=8, column=1, columnspan=3, pady=10, padx=10, sticky='we')

    fig, axes = plt.subplots(1, 3, figsize=(12, 3))
    fig.suptitle('Гистограммы насыщенности по каналам созданного изображения')

    img_array = np.array(img)

    axes[0].hist(img_array[:, :, 0].flatten(), bins=50, color='red', alpha=0.7, range=(0, 255))
    axes[0].set_title('Красный канал')
    axes[0].set_xlabel('Насыщенность')
    axes[0].set_ylabel('Частота')
    axes[0].grid(True, alpha=0.3)

    axes[1].hist(img_array[:, :, 1].flatten(), bins=50, color='green', alpha=0.7, range=(0, 255))
    axes[1].set_title('Зеленый канал')
    axes[1].set_xlabel('Насыщенность')
    axes[1].set_ylabel('Частота')
    axes[1].grid(True, alpha=0.3)

    axes[2].hist(img_array[:, :, 2].flatten(), bins=50, color='blue', alpha=0.7, range=(0, 255))
    axes[2].set_title('Синий канал')
    axes[2].set_xlabel('Насыщенность')
    axes[2].set_ylabel('Частота')
    axes[2].grid(True, alpha=0.3)

    plt.tight_layout()

    canvas = FigureCanvasTkAgg(fig, master=hist_frame)
    canvas.draw()
    canvas.get_tk_widget().pack(fill=tkinter.BOTH, expand=True)

    return canvas


def get_pick_name(cnt):
    if cnt % 2 == 0:
        return "../fuji.png"
    else:
        return "../yokohama.png"


def download_image():
    if created_image == '': return
    file_path = filedialog.asksaveasfilename(defaultextension=".png")
    if file_path:
        created_image.save(file_path)


def main():
    root = tkinter.Tk()
    root.title("Кузьмин Артемий Андреевич P3314 408941")
    frame = tkinter.Frame(root)
    frame.grid()

    tkinter.Button(root, text="Следующая картинка", command=lambda: open_image(root, get_pick_name(cnt)), padx=5,
                   pady=5).grid(row=1, column=1)
    tkinter.Button(root, text="Gray", command=lambda: create_gray_image(root), padx=5,
                   pady=5).grid(row=2, column=1)
    tkinter.Button(root, text="Инверсия", command=lambda: create_inverted_image(root), padx=5,
                   pady=5).grid(row=3, column=1)
    tkinter.Button(root, text="Блюр", command=lambda: create_blured_image(root), padx=5,
                   pady=5).grid(row=4, column=1)
    tkinter.Button(root, text="Скачать", command=lambda: download_image(), padx=5,
                   pady=5).grid(row=5, column=1)
    tkinter.Scale(root, from_=0.1, to=3.0, resolution=0.1, label='Яркость', orient='horizontal',
                  command=lambda x: on_brightness_slider_change(float(x), root)).grid(row=6, column=1)
    tkinter.Scale(root, from_=0.1, to=3.0, resolution=0.1, label='Контраст', orient='horizontal',
                  command=lambda x: on_contrast_slider_change(float(x), root)).grid(row=7, column=1)
    root.mainloop()


if __name__ == "__main__":
    main()
