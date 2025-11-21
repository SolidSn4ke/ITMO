import tkinter
from PIL import Image, ImageTk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

cnt = 0


def open_image(root, file_path):
    global cnt
    cnt += 1
    canvas = tkinter.Canvas(root, height=320, width=480)
    image = Image.open(file_path)
    photo = ImageTk.PhotoImage(image)
    canvas.create_image(0, 0, anchor='nw', image=photo)
    canvas.grid(row=1, column=2)

    image = Image.open(file_path).convert("RGB")
    red_p, green_p, blue_p = 0, 0, 0
    for i in range(image.width):
        for j in range(image.height):
            r, g, b = image.getpixel((i, j))
            red_p += r
            green_p += g
            blue_p += b
    red_p /= image.width * image.height
    green_p /= image.width * image.height
    blue_p /= image.width * image.height
    label = tkinter.Label(root, text=f"  {red_p:.2f}, {green_p:.2f}, {blue_p:.2f}  ")
    label.grid(row=1, column=4)
    create_histogram(root, round(red_p, 2), round(green_p, 2), round(blue_p, 2))
    root.mainloop()


def create_histogram(root, red_p, green_p, blue_p):
    fig, ax = plt.subplots(figsize=(5, 3), dpi=70)

    colors = ['red', 'green', 'blue']
    counts = [red_p, green_p, blue_p]
    labels = ['Красный', 'Зеленый', 'Синий']

    bars = ax.bar(labels, counts, color=colors, alpha=0.7, edgecolor='black')
    for bar, count in zip(bars, counts):
        ax.text(bar.get_x() + bar.get_width() / 2., max(map(lambda x: x.get_height(), bars)) + 15,
                f'{count:,}', ha='center', va='bottom', fontsize=9)
    canvas = FigureCanvasTkAgg(fig, master=root)
    canvas.draw()
    canvas.get_tk_widget().grid(row=1, column=3)


def get_pick_name():
    if cnt % 2 == 0:
        return "../fuji.png"
    else:
        return "../yokohama.png"


def main():
    root = tkinter.Tk()
    root.title("Кузьмин Артемий Андреевич P3314 408941")
    frame = tkinter.Frame(root)
    frame.grid()

    tkinter.Button(root, text="Следующая картинка", command=lambda: open_image(root, get_pick_name()), padx=5,
                   pady=5).grid(row=1, column=1)
    root.mainloop()


if __name__ == "__main__":
    main()
